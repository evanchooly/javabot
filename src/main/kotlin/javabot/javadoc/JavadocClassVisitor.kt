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
    lateinit val javadocClassDao: JavadocClassDao

    @Inject
    lateinit val apiDao: ApiDao

    @Inject
    lateinit val parser: JavadocParser

    lateinit var pkg: String

    lateinit var className: String

    private val packages = ArrayList<String>()

    override fun visit(version: Int, access: Int, name: String, signature: String,
                       superName: String?, interfaces: Array<String>) {
        try {
            pkg = getPackage(name)
            var process = packages.isEmpty()
            for (aPackage in packages) {
                process = process or pkg!!.startsWith(aPackage)
            }
            if (process && isPublic(access)) {
                className = name.substring(name.lastIndexOf("/") + 1).replace('$', '.')
                val javadocClass = parser.getOrCreate(parser.api, pkg, className!!)
                if (superName != null) {
                    val superPkg = getPackage(superName)
                    val parentName = superName.substring(superName.lastIndexOf("/") + 1)
                    val parent = parser.getOrQueue(parser.api, superPkg, parentName, javadocClass)
                    if (parent != null) {
                        javadocClass.setSuperClassId(parent)
                    }
                    javadocClassDao.save(javadocClass)
                }
            }
        } catch (e: Exception) {
            log.error(e.getMessage(), e)
            throw RuntimeException(e.getMessage(), e)
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

    override fun visitField(access: Int, name: String, desc: String, signature: String,
                            value: Any): FieldVisitor? {
        if (className != null) {
            val javadocClass = getJavadocClass()
            if (javadocClass != null && isPublic(access)) {
                try {
                    val javadocType = extractTypes(className!!, "", desc, false).get(0)
                    javadocClassDao.save(JavadocField(javadocClass, name, javadocType.toString()))
                } catch (e: IndexOutOfBoundsException) {
                    throw RuntimeException(e.getMessage(), e)
                }

            }
        }
        return null
    }

    private fun getJavadocClass(): JavadocClass? {
        val classes = javadocClassDao.getClass(parser.api, pkg, className)
        if (classes.size() == 1) {
            return classes[0]
        }
        throw RuntimeException("Wrong number of classes (%d) found for %s.%s".format(classes.size(),
              pkg, className))
    }

    override fun visitMethod(access: Int, name: String, desc: String, signature: String?,
                             exceptions: Array<String>): MethodVisitor? {
        if (className != null) {
            val javadocClass = getJavadocClass()
            if (javadocClass != null && (isPublic(access) || isProtected(access))) {
                val types = extractTypes(className, name, signature ?: desc,
                      (access and Opcodes.ACC_VARARGS) == Opcodes.ACC_VARARGS)

                val longTypes = ArrayList<String>()
                val shortTypes = ArrayList<String>()
                for (type in types) {
                    update(longTypes, shortTypes, type.toString())
                }
                var methodName: String
                if ("<init>" == name) {
                    methodName = javadocClass.name
                    if (methodName.contains(".")) {
                        methodName = methodName.substring(methodName.lastIndexOf(".") + 1)
                    }
                } else {
                    methodName = name
                }
                javadocClassDao.save(JavadocMethod(javadocClass, methodName,
                      types.size(), longTypes, shortTypes))
            }
        }
        return null
    }

    private fun update(longTypes: MutableList<String>, shortTypes: MutableList<String>, arg: String) {
        longTypes.add(arg)
        shortTypes.add(calculateNameAndPackage(arg)[1])
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

        public fun calculateNameAndPackage(value: String): List<String> {
            val list = arrayListOf<String>()
            var clsName = value
            while (clsName.contains(".") && Character.isLowerCase(clsName.charAt(0))) {
                clsName = clsName.substring(clsName.indexOf(".") + 1)
            }
            if (value != clsName) {
                list.add(value.substring(0, value.indexOf(clsName) - 1))
            }
            list.add(clsName)
            return list
        }

        fun extractTypes(className: String, methodName: String, signature: String,
                         varargs: Boolean): List<JavadocType> {
            val reader = SignatureReader(signature)
            val v = JavadocSignatureVisitor(className, methodName, signature, varargs)
            if (!signature.isEmpty()) {
                reader.accept(v)
            }
            return v.types
        }
    }
}