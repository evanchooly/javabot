package javabot.javadoc

import javabot.dao.ApiDao
import javabot.javadoc.JavadocType.JAVA11
import javabot.javadoc.JavadocType.JAVA17
import javabot.javadoc.JavadocType.JAVA6
import javabot.javadoc.JavadocType.JAVA7
import javabot.javadoc.JavadocType.JAVA8
import javabot.model.javadoc.JavadocApi
import javabot.model.javadoc.JavadocClass
import javabot.model.javadoc.JavadocField
import javabot.model.javadoc.JavadocMethod
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.ACC_VARARGS

class JavadocClassVisitor(
    val apiDao: ApiDao,
    var javadocApi: JavadocApi,
    var packageName: String,
    var className: String,
    var module: String?,
    val type: JavadocType,
) : ClassVisitor(Opcodes.ASM9) {

    private val interfaces = mutableListOf<String>()
    private var parentClass: String? = null
    val methods = mutableListOf<JavadocMethod>()
    val fields = mutableListOf<JavadocField>()

    lateinit var javadocClass: JavadocClass

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>,
    ) {
        if (access and Opcodes.ACC_PUBLIC == Opcodes.ACC_PUBLIC) {
            superName?.let { parentClass = it.replace("/", ".") }
            this.interfaces += interfaces.map { it.replace("/", ".") }

            javadocClass =
                JavadocClass(javadocApi, module, packageName, className.replace('$', '.')).also {
                    parentClass?.let { parent -> it.parentTypes += parent }
                    it.parentTypes += interfaces
                    apiDao.save(it)
                }
            super.visit(version, access, name, signature, superName, interfaces)
        }
    }

    override fun visitEnd() {
        for (method in methods) {
            apiDao.save(method)
        }
        for (field in fields) {
            apiDao.save(field)
        }
    }

    override fun visitField(
        access: Int,
        name: String,
        descriptor: String,
        signature: String?,
        value: Any?,
    ): FieldVisitor? {
        if (
            ::javadocClass.isInitialized and (access and Opcodes.ACC_PUBLIC == Opcodes.ACC_PUBLIC)
        ) {

            val urlFragment =
                when (type) {
                    JAVA6 -> "#${name}"
                    JAVA7 -> "#${name}"
                    JAVA8 -> "#${name}"
                    JAVA11 -> "#${name}"
                    else -> "#${name}"
                }
            fields += JavadocField(javadocClass, name, urlFragment)
        }
        return super.visitField(access, name, descriptor, signature, value)
    }

    override fun visitMethod(
        access: Int,
        name: String,
        descriptor: String,
        signature: String?,
        exceptions: Array<out String>?,
    ): MethodVisitor? {
        if (
            ::javadocClass.isInitialized and
                ((access and Opcodes.ACC_PUBLIC == Opcodes.ACC_PUBLIC) or
                    (access and Opcodes.ACC_PROTECTED == Opcodes.ACC_PROTECTED))
        ) {
            val mappedDescriptor = mapDescriptor(descriptor)
            val mappedSignature =
                try {
                    mapSignature(signature)
                } catch (e: java.lang.IllegalStateException) {
                    println("javadocClass = ${javadocClass}")
                    println(
                        "access = [${access}], name = [${name}], descriptor = [${descriptor}], signature = [${signature}], exceptions = [${exceptions}]"
                    )
                    throw e
                }

            for ((index, sig) in mappedSignature.withIndex()) {
                mappedDescriptor[index] = sig
            }

            val longArgs = mappedDescriptor.map { it.replace("/", ".") }.toMutableList()
            val shortArgs =
                mappedDescriptor
                    .map { it.substringAfterLast("/") }
                    .map { it.replace("/", ".") }
                    .toMutableList()

            if (access and ACC_VARARGS == ACC_VARARGS) {
                longArgs[longArgs.lastIndex] = longArgs.last().replace("[]", "...")
                shortArgs[shortArgs.lastIndex] = shortArgs.last().replace("[]", "...")
            }

            val urlFragment =
                when (type) {
                    JAVA6 -> TODO()
                    JAVA7 -> "#${name}${longArgs.joinToString("-", prefix = "-", postfix = "-")}"
                    JAVA8 -> "#${name}${longArgs.joinToString("-", prefix = "-", postfix = "-")}"
                    JAVA11 -> "#${name}${longArgs.joinToString(",", prefix = "(", postfix = ")")}"
                    JAVA17 -> "#${name}${longArgs.joinToString(",", prefix = "(", postfix = ")")}"
                }
            methods += JavadocMethod(javadocClass, name, urlFragment, longArgs, shortArgs)
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions)
    }

    fun mapSignature(type: String?) =
        if (type == null) listOf() else separate(type.substringAfter("(").substringBeforeLast(")"))

    fun separate(signature: String): List<String> {
        val separated = mutableListOf<String>()
        var index = 0
        while (index < signature.length) {
            val (s, i) = readNext(signature, index)
            index = i
            separated += s
        }

        return separated
    }

    private fun readNext(signature: String, index: Int): Pair<String, Int> {
        return when (signature[index]) {
            'T' -> readType(signature, index + 1)
            'L' -> readClass(signature, index + 1)
            'B' -> "byte" to (index + 1)
            'C' -> "char" to (index + 1)
            'D' -> "double" to (index + 1)
            'F' -> "float" to (index + 1)
            'I' -> "int" to (index + 1)
            'J' -> "long" to (index + 1)
            'S' -> "short" to (index + 1)
            'Z' -> "boolean" to (index + 1)
            '*' -> "*" to (index + 1)
            '[' -> readNext(signature, index + 1).let { "${it.first}[]" to it.second }
            '+' -> readNext(signature, index + 1).let { "? extends ${it.first}" to it.second }
            '-' -> readNext(signature, index + 1).let { "? super ${it.first}" to it.second }
            else -> throw IllegalStateException("${signature[index]} => ${signature}")
        }
    }

    private fun readClass(signature: String, index: Int): Pair<String, Int> {
        var type = ""
        var position = index
        while (signature[position] != ';') {
            when (signature[position]) {
                '<' -> {
                    type += signature[position++]
                    while (signature[position] != '>') {
                        readNext(signature, position).apply {
                            type += first
                            position = second
                        }
                        if (signature[position] != '>') {
                            type += ','
                        }
                    }
                    type += signature[position++]
                }
                else -> {
                    type += signature[position++]
                }
            }
        }
        return type.replace(Regex("<.*>"), "") to (position + 1)
    }

    private fun readType(signature: String, index: Int): Pair<String, Int> {
        var type = ""
        var position = index
        while (signature[position] != ';') {
            type += signature[position++]
        }
        return type to (position + 1)
    }

    fun mapDescriptor(type: String): MutableList<String> {
        val list = mutableListOf<String>()

        var i = 0
        val subbed = type.substringAfter("(").substringBeforeLast(")")
        while (i < subbed.length) {
            var array = ""
            while (subbed[i] == '[') {
                array += "[]"
                i++
            }
            val mapped =
                when (subbed[i++]) {
                    'B' -> "byte"
                    'C' -> "char"
                    'D' -> "double"
                    'F' -> "float"
                    'I' -> "int"
                    'J' -> "long"
                    'L' -> {
                        val semi = subbed.indexOf(';', i)
                        val found = subbed.substring(i, semi)
                        i = semi + 1
                        found
                    }
                    'S' -> "short"
                    'Z' -> "boolean"
                    else -> TODO("${subbed[i - 1]} not mapped: ${subbed}")
                }
            list += mapped + array
        }

        return list
    }

    override fun toString(): String {
        return "JavadocClassVisitor<${javadocClass.name}>)"
    }
}
