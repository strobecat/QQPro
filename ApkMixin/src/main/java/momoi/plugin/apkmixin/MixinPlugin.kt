package momoi.plugin.apkmixin

import com.android.apksigner.ApkSignerTool
import com.android.tools.smali.dexlib2.iface.ClassDef
import com.android.tools.smali.dexlib2.iface.DexFile
import com.android.tools.smali.dexlib2.immutable.ImmutableDexFile
import com.android.tools.smali.dexlib2.rewriter.DexFileRewriter
import com.android.tools.smali.dexlib2.rewriter.DexRewriter
import com.android.tools.smali.dexlib2.rewriter.Rewriter
import com.android.tools.smali.dexlib2.rewriter.RewriterModule
import com.android.tools.smali.dexlib2.rewriter.Rewriters
import com.wind.meditor.ManifestEditorMain
import lanchon.multidexlib2.BasicDexFileNamer
import lanchon.multidexlib2.DexIO
import lanchon.multidexlib2.MultiDexIO
import momoi.plugin.apkmixin.utils.Smali
import momoi.plugin.apkmixin.utils.ZipUtil
import momoi.plugin.apkmixin.utils.child
import momoi.plugin.apkmixin.utils.findClass
import momoi.plugin.apkmixin.utils.getDexCount
import momoi.plugin.apkmixin.utils.info
import momoi.plugin.apkmixin.utils.toClassDef
import momoi.plugin.apkmixin.utils.toSmali
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.io.FileNotFoundException
import java.util.zip.ZipFile

class MixinPlugin : Plugin<Project> {
    // TODO 写得一坨 有机会一定要写漂亮一点
    override fun apply(project: Project) {
        project.tasks.create("MixinApk") { task ->
            task.dependsOn("mergeDexRelease")
            task.doLast {
                val srcDex = MultiDexIO.readDexFile(
                    /* multiDex = */ true,
                    /* file = */ File(
                        project.projectDir,
                        "build/intermediates/dex/release/mergeDexRelease"
                    ),
                    /* namer = */ BasicDexFileNamer(),
                    /* opcodes = */ null,
                    /* logger = */ null
                )

                val targetApkName = Config.targetApk ?: throw IllegalArgumentException("targetApk must not be null")
                val targetApkFile = project.projectDir.child("mixin").child(targetApkName)
                val trgDex = MultiDexIO.readDexFile(
                    /* multiDex = */ true,
                    /* file = */ targetApkFile,
                    /* namer = */ BasicDexFileNamer(),
                    /* opcodes = */ null,
                    /* logger = */ null
                )
                val targetZipFile = ZipFile(targetApkFile)

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

                val newDex = MutableDexFile()
                val changedDef = mutableMapOf<ClassDef, ClassDef>()
                val modifiedClasses = mutableMapOf<String, ClassDef>()
                srcDex.classes.forEach { srcDef ->
                    var content = srcDef.toSmali()
                    mixinClasses.forEach { (rs, rt) ->
                        content = content.replace(rs.type, rt.type)
                    }
                    if (!mixinClasses.containsKey(srcDef)) {
                        if (trgDex.findClass(srcDef.type) == null) {
                            newDex.classes.add(content.toClassDef()!!)
                        }
                    } else {
                        val rawTrgDef = mixinClasses[srcDef]!!
                        val trgDef = changedDef[rawTrgDef] ?: rawTrgDef
                        val srcSmali = Smali(content)
                        val trgSmali = Smali(trgDef.toSmali())
                        srcSmali.methods.forEach { srcMethod ->
                            if (srcMethod.body.contains("Lmomoi/anno/mixin/StaticHook;")) {
                                srcMethod.modifiers.add("static")
                                if (srcMethod.name.endsWith("_")) {
                                    srcMethod.name = srcMethod.name.removeSuffix("_")
                                } else throw IllegalArgumentException("StaticHook method must end with '_': ${srcMethod.name}")
                            }
                            trgSmali.findMethod(srcMethod)?.let { trgMethod ->
                                if (srcMethod.body.contains("Lmomoi/anno/mixin/PrivateCall;")) {
                                    srcMethod.modifiers.remove("public")
                                    srcMethod.modifiers.remove("protected")
                                    srcMethod.modifiers.add("private")
                                }
                                srcMethod.body =
                                    srcMethod.body.replace("invoke-super", "invoke-virtual")
                                val rawCall = trgSmali.getMethodCall(trgMethod)
                                var index = 0
                                do {
                                    trgMethod.name += "_${index++}"
                                } while (trgSmali.methods.filter { it.name == trgMethod.name }.size > 1)
                                srcMethod.body = srcMethod.body.replace(
                                    rawCall,
                                    trgSmali.getMethodCall(trgMethod)
                                )
                            }
                            trgSmali.methods.add(srcMethod)
                        }
                        srcSmali.fields.forEach { srcField ->
                            if (trgSmali.fields.find { it.name == srcField.name } == null) {
                                trgSmali.fields.add(srcField)
                            }
                        }
                        val newTrgDef = trgSmali.toText().toClassDef()!!
                        changedDef[trgDef] = newTrgDef
                        modifiedClasses[newTrgDef.type] = newTrgDef
                    }
                }

                val rewriter = DexRewriter(
                    object : RewriterModule() {
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
                    }
                )
                info("Saving dex...")
                val namer = BasicDexFileNamer()
                val outputDexDir = project.projectDir.child("build/mixinDex")
                MultiDexIO.writeDexFile(
                    /* multiDex = */ true,
                    /* threadCount = */ targetZipFile.getDexCount(namer),
                    /* file = */ project.projectDir.child("build/mixinDex"),
                    /* namer = */ namer,
                    /* dexFile = */ rewriter.dexFileRewriter.rewrite(trgDex),
                    /* maxDexPoolSize = */ DexIO.DEFAULT_MAX_DEX_POOL_SIZE,
                    /* logger = */ null
                )
                info("Zip to mixin.apk...")
                ZipUtil.addOrReplaceFilesInZip(
                    project.projectDir.child("dist/mixin.apk"),
                    outputDexDir.listFiles()?.associateBy { it.name } ?: emptyMap()
                )
            }
        }
        project.tasks.create("MixinAPK-debug") {
            it.dependsOn("MixinApk")
            it.doLast {
                ManifestEditorMain.main(
                    project.projectDir.child("dist/mixin.apk").absolutePath,
                    "-o",
                    project.projectDir.child("dist/unsign.apk").absolutePath,
                    "-d", "1",
                    "-vn", Config.versionName,
                    "--force"
                )
                sign(project)
            }
        }
        project.tasks.create("MixinAPK-release") {
            it.dependsOn("MixinApk")
            it.doLast {
                ManifestEditorMain.main(
                    project.projectDir.child("dist/mixin.apk").absolutePath,
                    "-o",
                    project.projectDir.child("dist/unsign.apk").absolutePath,
                    "-d", "0",
                    "-vn", Config.versionName,
                    "--force"
                )
                sign(project)
            }
        }
    }

    fun sign(project: Project) {
        ApkSignerTool.main(arrayOf(
            "sign",
            "--key",
            project.projectDir.child("dist/testkey.pk8").absolutePath,
            "--cert",
            project.projectDir.child("dist/testkey.x509.pem").absolutePath,
            "--out",
            project.projectDir.child("dist/signed.apk").absolutePath,
            project.projectDir.child("dist/unsign.apk").absolutePath
        ))
        project.projectDir.child("dist/unsign.apk").delete()
    }

}