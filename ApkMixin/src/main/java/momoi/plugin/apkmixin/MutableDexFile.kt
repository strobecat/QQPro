package momoi.plugin.apkmixin

import org.jf.dexlib2.DexFileFactory
import org.jf.dexlib2.Opcodes
import org.jf.dexlib2.iface.ClassDef
import org.jf.dexlib2.iface.DexFile
import java.io.File

class MutableDexFile() : DexFile {
    constructor(src: DexFile) : this() {
        classes.addAll(src.classes)
    }

    private val classes = mutableSetOf<ClassDef>()

    override fun getClasses(): MutableSet<ClassDef> {
        return classes
    }

    override fun getOpcodes() = Opcodes.getDefault()

    fun writeTo(file: File) {
        DexFileFactory.writeDexFile(file.absolutePath, this)
    }
}