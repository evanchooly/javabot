package javabot.javadoc

import javabot.dao.ApiDao
import javabot.dao.JavadocClassDao
import javabot.javadoc.JavadocSignatureVisitor.JavadocType
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.signature.SignatureReader
import org.slf4j.LoggerFactory
import java.util.ArrayList
import java.util.Arrays
import java.util.HashMap
import javax.inject.Inject

public class JavadocClassVisitor : ClassVisitor(Opcodes.ASM5) {

    @Inject
    lateinit var javadocClassDao: JavadocClassDao

    @Inject
    lateinit var apiDao: ApiDao

    @Inject
    lateinit var parser: JavadocParser

    lateinit var pkg: String

    lateinit var className: String

    private val packages = ArrayList<String>()

    override fun visit(version: Int, access: Int, name: String, signature: String?,
                       superName: String?, interfaces: Array<String>?) {
        try {
            pkg = getPackage(name)
            var process = packages.isEmpty()
            for (aPackage in packages) {
                process = process || pkg.startsWith(aPackage)
            }
            className = name.substring(name.lastIndexOf("/") + 1).replace('$', '.')
            if (process && isPublic(access)) {
                val javadocClass = parser.getJavadocClass(parser.api, pkg, className)
                if (superName != null) {
                    val superPkg = getPackage(superName)
                    val parentName = superName.substring(superName.lastIndexOf("/") + 1)
                    val parent = parser.getJavadocClass(parser.api, superPkg, parentName)
                    javadocClass.setSuperClassId(parent)
                    javadocClassDao.save(javadocClass)
                }
            }
        } catch (e: Exception) {
            log.error(e.message, e)
            throw RuntimeException(e.message, e)
        }

    }

    private fun isPublic(access: Int): Boolean {
        return (access and Opcodes.ACC_PUBLIC) == Opcodes.ACC_PUBLIC
    }

    protected fun isProtected(access: Int): Boolean {
        return (access and Opcodes.ACC_PROTECTED) == Opcodes.ACC_PROTECTED
    }

    private fun getPackage(name: String): String {
        return if (name.contains("/"))
            name.substring(0, name.lastIndexOf("/")).replace('/', '.')
        else
            ""
    }

    override fun visitField(access: Int, name: String, desc: String, signature: String?,
                            value: Any?): FieldVisitor? {
        val javadocClass = getJavadocClass()
        if (javadocClass != null && isPublic(access)) {
            try {
                val javadocType = extractTypes(className, "", desc, false)[0]
                javadocClassDao.save(JavadocField(javadocClass, name, javadocType.toString()))
            } catch (e: IndexOutOfBoundsException) {
                throw RuntimeException(e.message, e)
            }
        }
        return null
    }

    private fun getJavadocClass(): JavadocClass? {
        return parser.getJavadocClass(parser.api, pkg, className)
    }

    override fun visitMethod(access: Int, name: String, desc: String?, signature: String?,
                             exceptions: Array<String>?): MethodVisitor? {
        val javadocClass = getJavadocClass()
        if (javadocClass != null && (isPublic(access) || isProtected(access))) {
            val types = extractTypes(className, name, signature ?: desc,
                    (access and Opcodes.ACC_VARARGS) == Opcodes.ACC_VARARGS)

            val longTypes = ArrayList<String>()
            val shortTypes = ArrayList<String>()
            for (type in types) {
                update(longTypes, shortTypes, type.toString())
            }
            val methodName = if ("<init>" == name) javadocClass.name.split('.').last() else name
            javadocClassDao.save(JavadocMethod(javadocClass, methodName, types.size, longTypes, shortTypes))
        }
        return null
    }

    private fun update(longTypes: MutableList<String>, shortTypes: MutableList<String>, arg: String) {
        longTypes.add(arg)
        shortTypes.add(calculateNameAndPackage(arg).second)
    }

    public fun setPackages(vararg packages: String) {
        this.packages.addAll(Arrays.asList(*packages))
    }

    companion object {
        private val log = LoggerFactory.getLogger(JavadocClassVisitor::class.java)

        private val PRIMITIVES = HashMap<Char, String>()

        init {
            PRIMITIVES.put('B', "byte")
            PRIMITIVES.put('C', "char")
            PRIMITIVES.put('D', "double")
            PRIMITIVES.put('F', "float")
            PRIMITIVES.put('I', "int")
            PRIMITIVES.put('J', "long")
            PRIMITIVES.put('S', "short")
            PRIMITIVES.put('Z', "boolean")
        }

        public fun calculateNameAndPackage(value: String): Pair<String?, String> {
            var clsName = value
            while (clsName.contains(".") && Character.isLowerCase(clsName[0])) {
                clsName = clsName.substring(clsName.indexOf(".") + 1)
            }
            val pkgName = if (value != clsName) {
                value.substring(0, value.indexOf(clsName) - 1)
            } else {
                null
            }

            return Pair(pkgName, clsName)
        }

        fun extractTypes(className: String, methodName: String, signature: String?,
                         varargs: Boolean): List<JavadocType> {
            val reader = SignatureReader(signature)
            val v = JavadocSignatureVisitor(className, methodName, signature, varargs)
            if (!(signature?.isEmpty() ?: true)) {
                reader.accept(v)
            }
            return v.types
        }
    }
}