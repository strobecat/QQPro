package momoi.plugin.apkmixin

import com.android.apksigner.ApkSignerTool
import com.wind.meditor.ManifestEditorMain
import momoi.plugin.apkmixin.utils.child
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.file.RegularFile
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

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
        project.tasks.register("MixinApk", MixinApkTask::class.java) {
            val targetApkName = Config.targetApk ?: throw IllegalArgumentException("targetApk must not be null")
            it.mixinAppDex = project.fileTree("build/intermediates/dex/release/mergeDexRelease")
            it.targetAppFile = project.layout.projectDirectory.dir("mixin").file(targetApkName)
            it.doLast {
                createMetadata(project)
            }
        }
    }

    private fun createMixinDebugTask(project: Project) {
        project.tasks.create("MixinApk-debug") {
            it.dependsOn("MixinApk")
            it.doLast {
                processManifest(project, isDebug = true)
                sign(project)
                createMetadata(project, "signed.apk")
            }
        }
    }

    private fun createMixinReleaseTask(project: Project) {
        project.tasks.create("MixinApk-release") {
            it.dependsOn("MixinApk")
            it.doLast {
                processManifest(project, isDebug = false)
                sign(project)
                createMetadata(project, "signed.apk")
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

    private fun createMetadata(
        project: Project,
        fileName: String = "mixin.apk"
    ) {
        createRedirectFile(project)
        createOutputMetadataFile(project, fileName)
    }

    private fun createRedirectFile(project: Project) {
        project.projectDir.child("build/intermediates/apk_ide_redirect_file/debug/createDebugApkListingFileRedirect/redirect.txt")
            .writeText("""
                #- File Locator -
                listingFile=../../../../../dist/output-metadata.json
            """.trimIndent())
    }

    private fun createOutputMetadataFile(
        project: Project,
        fileName: String
    ) {
        project.projectDir.child("dist/output-metadata.json").writeText("""
            {
              "version": 3,
              "artifactType": {
                "type": "APK",
                "kind": "Directory"
              },
              "applicationId": "com.tencent.qqlite",
              "variantName": "debug",
              "elements": [
                {
                  "type": "SINGLE",
                  "filters": [],
                  "attributes": [],
                  "outputFile": "$fileName"
                }
              ],
              "elementType": "File",
              "minSdkVersionForDexing": 24
            }
        """.trimIndent())
    }
}

abstract class MixinApkTask: DefaultTask() {
    @get:InputFiles
    abstract var mixinAppDex: FileCollection

    @get:InputFile
    abstract var targetAppFile: RegularFile

    @get:OutputFile
    val outputFile: RegularFile = project.layout.projectDirectory.file("dist/mixin.apk")

    init {
        dependsOn("mergeDexRelease")
    }

    @TaskAction
    fun execute() {
        val processor = MixinProcessor(project, mixinAppDex.singleFile.parentFile, targetAppFile.asFile)
        processor.processMixin()
    }

}