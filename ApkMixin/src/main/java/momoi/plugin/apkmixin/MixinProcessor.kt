package momoi.plugin.apkmixin

import com.android.tools.smali.dexlib2.iface.ClassDef
import com.android.tools.smali.dexlib2.iface.DexFile
import com.android.tools.smali.dexlib2.immutable.ImmutableDexFile
import com.android.tools.smali.dexlib2.rewriter.DexFileRewriter
import com.android.tools.smali.dexlib2.rewriter.DexRewriter
import com.android.tools.smali.dexlib2.rewriter.Rewriter
import com.android.tools.smali.dexlib2.rewriter.RewriterModule
import com.android.tools.smali.dexlib2.rewriter.Rewriters
import lanchon.multidexlib2.BasicDexFileNamer
import lanchon.multidexlib2.DexIO
import lanchon.multidexlib2.MultiDexIO
import momoi.plugin.apkmixin.utils.Smali
import momoi.plugin.apkmixin.utils.SmaliMethod
import momoi.plugin.apkmixin.utils.ZipUtil
import momoi.plugin.apkmixin.utils.child
import momoi.plugin.apkmixin.utils.findClass
import momoi.plugin.apkmixin.utils.getDexCount
import momoi.plugin.apkmixin.utils.info
import momoi.plugin.apkmixin.utils.toClassDef
import momoi.plugin.apkmixin.utils.toSmali
import org.gradle.api.Project
import java.io.File
import java.io.FileNotFoundException
import java.util.zip.ZipFile

class MixinProcessor(private val project: Project) {

    val targetApkName = Config.targetApk ?: throw IllegalArgumentException("targetApk must not be null")

    fun processMixin() {
        val (srcDex, trgDex, targetZipFile) = loadDexFiles()
        val mixinClasses = findMixinClasses(srcDex, trgDex)
        val modifiedClasses = processClasses(srcDex, trgDex, mixinClasses)
        writeDexOutput(trgDex, modifiedClasses, targetZipFile)
    }

    private fun loadDexFiles(): Triple<DexFile, DexFile, ZipFile> {
        val srcDex = MultiDexIO.readDexFile(
            /* multiDex = */ true,
            /* file = */ File(project.projectDir, "build/intermediates/dex/release/mergeDexRelease"),
            /* namer = */ BasicDexFileNamer(),
            /* opcodes = */ null,
            /* logger = */ null
        )

        val targetApkFile = project.projectDir.child("mixin").child(targetApkName)
        val trgDex = MultiDexIO.readDexFile(
            /* multiDex = */ true,
            /* file = */ targetApkFile,
            /* namer = */ BasicDexFileNamer(),
            /* opcodes = */ null,
            /* logger = */ null
        )
        val targetZipFile = ZipFile(targetApkFile)

        return Triple(srcDex, trgDex, targetZipFile)
    }

    private fun findMixinClasses(srcDex: DexFile, trgDex: DexFile): Map<ClassDef, ClassDef> {
        val mixinClasses = mutableMapOf<ClassDef, ClassDef>()
        srcDex.classes.forEach { srcDef ->
            if (srcDef.annotations.any { it.type == "Lmomoi/anno/mixin/Mixin;" }) {
                mixinClasses[srcDef] = srcDef.superclass?.let { s -> trgDex.findClass(s) }
                    ?: throw FileNotFoundException(
                        "Cannot find mixin ${srcDef.type} target class ${srcDef.superclass}"
                    )
                info("Find Mixin Class: ${srcDef.type} to ${srcDef.superclass}")
            }
        }
        return mixinClasses
    }

    private fun processClasses(
        srcDex: DexFile,
        trgDex: DexFile,
        mixinClasses: Map<ClassDef, ClassDef>
    ): Map<String, ClassDef> {
        val newDex = MutableDexFile()
        val changedDef = mutableMapOf<ClassDef, ClassDef>()
        val modifiedClasses = mutableMapOf<String, ClassDef>()

        srcDex.classes.forEach { srcDef ->
            var content = srcDef.toSmali()
            mixinClasses.forEach { (rs, rt) ->
                content = content.replace(rs.type, rt.type)
            }

            when {
                !mixinClasses.containsKey(srcDef) -> {
                    if (trgDex.findClass(srcDef.type) == null) {
                        newDex.classes.add(content.toClassDef()!!)
                    }
                }
                else -> mixinToTargetClass(srcDef, content, mixinClasses, changedDef, modifiedClasses)
            }
        }

        return modifiedClasses
    }

    private fun mixinToTargetClass(
        srcDef: ClassDef,
        content: String,
        mixinClasses: Map<ClassDef, ClassDef>,
        changedDef: MutableMap<ClassDef, ClassDef>,
        modifiedClasses: MutableMap<String, ClassDef>
    ) {
        val rawTrgDef = mixinClasses[srcDef]!!
        val trgDef = changedDef[rawTrgDef] ?: rawTrgDef
        val srcSmali = Smali(content)
        val trgSmali = Smali(trgDef.toSmali())

        processMethods(srcSmali, trgSmali)
        processFields(srcSmali, trgSmali)

        val newTrgDef = trgSmali.toText().toClassDef()!!
        changedDef[trgDef] = newTrgDef
        modifiedClasses[newTrgDef.type] = newTrgDef
    }

    private fun processMethods(srcSmali: Smali, trgSmali: Smali) {
        srcSmali.methods.forEach { srcMethod ->
            processMethodStaticHook(srcMethod)
            trgSmali.findMethod(srcMethod)?.let { trgMethod ->
                processMethodBody(srcMethod, trgMethod, trgSmali)
            }
            trgSmali.methods.add(srcMethod)
        }
    }

    private fun processMethodStaticHook(srcMethod: SmaliMethod) {
        if (srcMethod.body.contains("Lmomoi/anno/mixin/StaticHook;")) {
            srcMethod.modifiers.add("static")
            if (srcMethod.name.endsWith("_")) {
                srcMethod.name = srcMethod.name.removeSuffix("_")
            } else throw IllegalArgumentException("StaticHook method must end with '_': ${srcMethod.name}")
        }
    }

    private fun processMethodBody(srcMethod: SmaliMethod, trgMethod: SmaliMethod, trgSmali: Smali) {
        if (srcMethod.body.contains("Lmomoi/anno/mixin/PrivateCall;")) {
            srcMethod.modifiers.remove("public")
            srcMethod.modifiers.remove("protected")
            srcMethod.modifiers.add("private")
        }
        srcMethod.body = srcMethod.body.replace("invoke-super", "invoke-virtual")
        val rawCall = trgSmali.getMethodCall(trgMethod)
        var index = 0
        do {
            trgMethod.name += "_${index++}"
        } while (trgSmali.methods.filter { it.name == trgMethod.name }.size > 1)
        srcMethod.body = srcMethod.body.replace(rawCall, trgSmali.getMethodCall(trgMethod))
    }

    private fun processFields(srcSmali: Smali, trgSmali: Smali) {
        srcSmali.fields.forEach { srcField ->
            if (!trgSmali.fields.any { it.name == srcField.name }) {
                trgSmali.fields.add(srcField)
            }
        }
    }

    private fun writeDexOutput(trgDex: DexFile, modifiedClasses: Map<String, ClassDef>, targetZipFile: ZipFile) {
        val rewriter = createDexRewriter(modifiedClasses)
        info("Saving dex...")
        val namer = BasicDexFileNamer()
        val outputDexDir = project.projectDir.child("build/mixinDex")
        
        MultiDexIO.writeDexFile(
            /* multiDex = */ true,
            /* threadCount = */ targetZipFile.getDexCount(namer),
            /* file = */ outputDexDir,
            /* namer = */ namer,
            /* dexFile = */ rewriter.dexFileRewriter.rewrite(trgDex),
            /* maxDexPoolSize = */ DexIO.DEFAULT_MAX_DEX_POOL_SIZE,
            /* logger = */ null
        )

        info("Zip to mixin.apk...")
        val mixinApkFile = project.projectDir.child("dist/mixin.apk")
        val targetApkFile = project.projectDir.child("mixin").child(targetApkName)
        if (mixinApkFile.length() != targetApkFile.length())
            targetApkFile.copyTo(mixinApkFile, overwrite = true)
        ZipUtil.addOrReplaceFilesInZip(
            mixinApkFile,
            outputDexDir.listFiles()?.associateBy { it.name } ?: emptyMap()
        )
    }

    private fun createDexRewriter(modifiedClasses: Map<String, ClassDef>): DexRewriter {
        return DexRewriter(object : RewriterModule() {
            override fun getDexFileRewriter(rewriters: Rewriters): Rewriter<DexFile?> {
                return object : DexFileRewriter(rewriters) {
                    override fun rewrite(value: DexFile): DexFile {
                        return super.rewrite(
                            ImmutableDexFile(
                                value.opcodes,
                                buildList {
                                    addAll(value.classes)
                                    val types = modifiedClasses.keys
                                    removeAll { it.type in types }
                                    addAll(modifiedClasses.values)
                                }
                            )
                        )
                    }
                }
            }
        })
    }
}
