package momoi.plugin.apkmixin

import momoi.plugin.apkmixin.utils.info
import org.jf.dexlib2.DexFileFactory
import org.jf.dexlib2.Opcodes
import org.jf.dexlib2.iface.ClassDef
import org.jf.dexlib2.iface.DexFile
import java.io.File
import kotlin.concurrent.thread

class MultiDex {
    companion object {
        private fun parseDex(file: File) = DexFileFactory.loadDexFile(file, Opcodes.getDefault())
        fun fromDir(dir: File) = MultiDex().apply {
            dir.listFiles { file -> file.extension == "dex" }?.forEach {
                dexList.add(MutableDexFile(parseDex(it)))
            }
        }
    }

    val dexList = mutableListOf<MutableDexFile>()

    inline fun foreachClasses(block: (ClassDef) -> Unit) = dexList.forEach {
        it.classes.forEach(block)
    }

    fun findClass(type: String): ClassDef? {
        foreachClasses {
            if (it.type == type) {
                return it
            }
        }
        return null
    }

    fun saveTo(dir: File): List<File> {
        val tasks = dexList.mapIndexed { index, dexFile ->
            val targetFile = File(dir, "classes${if (index != 0) index + 1 else ""}.dex").apply {
                parentFile?.mkdirs()
                delete()
            }
            thread(name = targetFile.name) { dexFile.writeTo(targetFile) } to targetFile
        }
        tasks.forEach { (thread, _) -> thread.join() }
        return tasks.map { it.second }
    }
}