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

const val APK_MIXIN_EXTENSION_NAME = "apkMixin"

class MixinPlugin : Plugin<Project> {

    private lateinit var extension: ApkMixinExtension

    override fun apply(project: Project) {
        extension = project.extensions.create(APK_MIXIN_EXTENSION_NAME, ApkMixinExtension::class.java)
        configureMixinTasks(project)
    }

    private fun configureMixinTasks(project: Project) {
        createMixinBaseTask(project)
        createMixinDebugTask(project)
        createMixinReleaseTask(project)
    }

    private fun createMixinBaseTask(project: Project) {
        project.tasks.register("MixinApk", MixinApkTask::class.java) {
            val targetApkName = extension.targetApk ?: throw IllegalArgumentException("targetApk must not be null")
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
                createMetadata(project, extension.output.signedFileName)
            }
        }
    }

    private fun createMixinReleaseTask(project: Project) {
        project.tasks.create("MixinApk-release") {
            it.dependsOn("MixinApk")
            it.doLast {
                processManifest(project, isDebug = false)
                sign(project)
                createMetadata(project, extension.output.signedFileName)
            }
        }
    }

    private fun processManifest(project: Project, isDebug: Boolean) {
        ManifestEditorMain.main(
            project.outputDir(extension).child(extension.output.mixinApkFileName).absolutePath,
            "-o",
            project.outputDir(extension).child(extension.output.unsignedFileName).absolutePath,
            "-d", if (isDebug) "1" else "0",
            "-vn", extension.versionName,
            "--force"
        )
    }

    private fun sign(project: Project) {
        if (extension.signing.enabled) {
            val unsignedApkFile = project.outputDir(extension).child(extension.output.unsignedFileName)
            ApkSignerTool.main(arrayOf(
                "sign",
                "--key", extension.signing.keyFile?.absolutePath,
                "--cert", extension.signing.certFile?.absolutePath,
                "--out", project.outputDir(extension).child(extension.output.signedFileName).absolutePath,
                unsignedApkFile.absolutePath
            ))
            unsignedApkFile.delete()
        }
    }

    private fun createMetadata(
        project: Project,
        fileName: String = extension.output.mixinApkFileName
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
    val outputFile: RegularFile = project.layout.projectDirectory.file("dist/${DEFAULT_MIXIN_APK_NAME}")

    init {
        dependsOn("mergeDexRelease")
    }

    @TaskAction
    fun execute() {
        val processor = MixinProcessor(project, mixinAppDex.singleFile.parentFile, targetAppFile.asFile)
        processor.processMixin()
    }

}