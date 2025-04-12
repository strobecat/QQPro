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
    override fun apply(project: Project) {
        configureMixinTasks(project)
    }

    private fun configureMixinTasks(project: Project) {
        createMixinBaseTask(project)
        createMixinDebugTask(project)
        createMixinReleaseTask(project)
    }

    private fun createMixinBaseTask(project: Project) {
        project.tasks.create("MixinApk") { task ->
            task.dependsOn("mergeDexRelease")
            task.doLast {
                val processor = MixinProcessor(project)
                processor.processMixin()
                createMetadata(project)
            }
        }
    }

    private fun createMixinDebugTask(project: Project) {
        project.tasks.create("MixinAPK-debug") {
            it.dependsOn("MixinApk")
            it.doLast {
                processManifest(project, isDebug = true)
                sign(project)
            }
        }
    }

    private fun createMixinReleaseTask(project: Project) {
        project.tasks.create("MixinAPK-release") {
            it.dependsOn("MixinApk")
            it.doLast {
                processManifest(project, isDebug = false)
                sign(project)
            }
        }
    }

    private fun processManifest(project: Project, isDebug: Boolean) {
        ManifestEditorMain.main(
            project.projectDir.child("dist/mixin.apk").absolutePath,
            "-o",
            project.projectDir.child("dist/unsign.apk").absolutePath,
            "-d", if (isDebug) "1" else "0",
            "-vn", Config.versionName,
            "--force"
        )
    }

    private fun createMetadata(project: Project) {
        createRedirectFile(project)
        createOutputMetadataFile(project)
    }

    private fun createRedirectFile(project: Project) {
        project.projectDir.child("build/intermediates/apk_ide_redirect_file/debug/createDebugApkListingFileRedirect/redirect.txt")
            .writeText("""
                #- File Locator -
                listingFile=../../../../../dist/output-metadata.json
            """.trimIndent())
    }

    private fun createOutputMetadataFile(project: Project) {
        project.projectDir.child("dist/output-metadata.json").writeText("""
            {
              "version": 3,
              "artifactType": {
                "type": "APK",
                "kind": "Directory"
              },
              "applicationId": "com.skywear.keyuploader",
              "variantName": "debug",
              "elements": [
                {
                  "type": "SINGLE",
                  "filters": [],
                  "attributes": [],
                  "outputFile": "mixin.apk"
                }
              ],
              "elementType": "File",
              "minSdkVersionForDexing": 24
            }
        """.trimIndent())
    }

    private fun sign(project: Project) {
        ApkSignerTool.main(arrayOf(
            "sign",
            "--key", project.projectDir.child("dist/testkey.pk8").absolutePath,
            "--cert", project.projectDir.child("dist/testkey.x509.pem").absolutePath,
            "--out", project.projectDir.child("dist/signed.apk").absolutePath,
            project.projectDir.child("dist/unsign.apk").absolutePath
        ))
        project.projectDir.child("dist/unsign.apk").delete()
    }
}

