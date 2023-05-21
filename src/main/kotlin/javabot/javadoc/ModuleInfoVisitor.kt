package javabot.javadoc

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ModuleVisitor
import org.objectweb.asm.Opcodes

class ModuleInfoVisitor() : ClassVisitor(Opcodes.ASM9) {

    var module: String? = null

    override fun visitModule(name: String?, access: Int, version: String?): ModuleVisitor? {
        module = name
        return super.visitModule(name, access, version)
    }

    override fun toString(): String {
        return "ModuleInfoVisitor<${module}>)"
    }
}
