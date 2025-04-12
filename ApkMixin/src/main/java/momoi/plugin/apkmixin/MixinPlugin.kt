package momoi.plugin.apkmixin

import com.android.apksigner.ApkSignerTool
import com.wind.meditor.ManifestEditorMain
import momoi.plugin.apkmixin.utils.Smali
import momoi.plugin.apkmixin.utils.ZipUtil
import momoi.plugin.apkmixin.utils.child
import momoi.plugin.apkmixin.utils.info
import momoi.plugin.apkmixin.utils.toClassDef
import momoi.plugin.apkmixin.utils.toSmali
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jf.dexlib2.iface.ClassDef
import java.io.File
import java.io.FileNotFoundException

class MixinPlugin : Plugin<Project> {
    //TODO 写得一坨 有机会一定要写漂亮一点
    override fun apply(project: Project) {
        project.tasks.create("MixinApk") { task ->
            task.dependsOn("mergeDexRelease")
            task.doLast {
                val srcDex = MultiDex.fromDir(
                    File(
                        project.projectDir,
                        "build/intermediates/dex/release/mergeDexRelease"
                    )
                )
                val trgDex = MultiDex.fromDir(
                    project.projectDir.child("mixin/target")
                )
                val mixinClasses = mutableMapOf<ClassDef, ClassDef>()
                srcDex.foreachClasses { srcDef ->
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
                srcDex.foreachClasses { srcDef ->
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
                        trgDex.dexList.forEach { subDex ->
                            subDex.classes.find { it.type == trgDef.type }?.let { subDef ->
                                subDex.classes.remove(subDef)
                                subDex.classes.add(newTrgDef)
                            }
                        }
                    }
                }
                trgDex.dexList.add(newDex)
                info("Saving dex...")
                val result = trgDex.saveTo(project.projectDir.child("build/mixinDex"))
                info("Zip to mixin.apk...")
                ZipUtil.addOrReplaceFilesInZip(
                    project.projectDir.child("dist/mixin.apk"),
                    result.associateBy { it.name }
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
                    "-vn", Config.versionName
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
                    "-vn", Config.versionName
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