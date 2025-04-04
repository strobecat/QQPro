package momoi.plugin.apkmixin.utils

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

object ZipUtil {
    fun addOrReplaceFilesInZip(zipFile: File, filesToAdd: Map<String, File>) {
        // 创建临时文件
        val tempFile = File.createTempFile("tempZip", ".zip", zipFile.parentFile)
        try {
            ZipOutputStream(tempFile.outputStream().buffered()).use { zos ->
                // 处理原始 ZIP 条目，跳过需替换的文件
                ZipFile(zipFile).use { originalZip ->
                    originalZip.entries().asSequence().forEach { entry ->
                        if (!filesToAdd.containsKey(entry.name)) {
                            // 复制条目到临时 ZIP
                            zos.putNextEntry(ZipEntry(entry.name).apply {
                                time = entry.time
                                comment = entry.comment
                            })
                            originalZip.getInputStream(entry).copyTo(zos)
                            zos.closeEntry()
                        }
                    }
                }

                // 添加或替换新文件
                filesToAdd.forEach { (entryName, file) ->
                    zos.putNextEntry(ZipEntry(entryName))
                    file.inputStream().use { it.copyTo(zos) }
                    zos.closeEntry()
                }
            }

            // 替换原文件
            if (!zipFile.delete() || !tempFile.renameTo(zipFile)) {
                throw IOException("Failed to replace original ZIP file")
            }
        } catch (e: Exception) {
            tempFile.delete() // 清理临时文件
            throw e
        }
    }
}