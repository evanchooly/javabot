package javabot.javadoc

import org.objectweb.asm.Opcodes
import org.objectweb.asm.signature.SignatureVisitor
import org.slf4j.LoggerFactory
import java.util.ArrayList
import java.util.HashMap
import java.util.Stack

class JavadocSignatureVisitor(private val className: String, private val name: String, private val signature: String,
                              private val varargs: Boolean) : SignatureVisitor(Opcodes.ASM5) {

    var trace = LOG.isTraceEnabled

    val types = ArrayList<JavadocType>()

    var type: JavadocType? = null

    var done = false

    val stack = Stack<String>()

    init {
        if (trace) {
            LOG.trace("\n\n*** JavadocSignatureVisitor.JavadocSignatureVisitor")
            LOG.trace("className = [$className], name = [$name],\n signature = [$signature]")
        }
    }

    override fun visitFormalTypeParameter(formalParameter: String) {
        push("JavadocSignatureVisitor.visitFormalTypeParameter($formalParameter)")
        type = JavadocType(formalParameter)
    }

    override fun visitClassBound(): SignatureVisitor {
        push("JavadocSignatureVisitor.visitClassBound")
        return this
    }

    override fun visitInterfaceBound(): SignatureVisitor {
        push("JavadocSignatureVisitor.visitInterfaceBound")
        return this
    }

    override fun visitSuperclass(): SignatureVisitor {
        push("JavadocSignatureVisitor.visitSuperclass")
        return this
    }

    override fun visitInterface(): SignatureVisitor {
        push("JavadocSignatureVisitor.visitInterface")
        return this
    }

    override fun visitParameterType(): SignatureVisitor {
        push("JavadocSignatureVisitor.visitParameterType")
        type = JavadocType()
        types.add(type!!)
        return this
    }

    override fun visitReturnType(): SignatureVisitor {
        push("JavadocSignatureVisitor.visitReturnType")
        done = true
        type = JavadocType()
        return this
    }

    override fun visitExceptionType(): SignatureVisitor {
        push("JavadocSignatureVisitor.visitExceptionType")
        type = JavadocType()
        return this
    }

    override fun visitBaseType(baseType: Char) {
        if (trace) {
            LOG.trace("JavadocSignatureVisitor.visitBaseType($baseType)")
        }
        if (type == null) {
            type = JavadocType()
            types.add(type!!)
        }
        type!!.name = PRIMITIVES.get(baseType)
    }

    override fun visitTypeVariable(typeVariable: String) {
        if (trace) {
            LOG.trace("JavadocSignatureVisitor.visitTypeVariable($typeVariable)")
        }
        if (!done) {
            type!!.addTypeVariable(typeVariable)
        }
    }

    override fun visitArrayType(): SignatureVisitor {
        push("JavadocSignatureVisitor.visitArrayType")
        if (type == null) {
            type = JavadocType()
            types.add(type!!)
        }
        type!!.setArray(true)
        //    if(1==1) throw new RuntimeException("JavadocSignatureVisitor.visitArrayType");
        return this
    }

    override fun visitClassType(classType: String?) {
        push("JavadocSignatureVisitor.visitClassType($classType)")
        if (type == null) {
            type = JavadocType()
            types.add(type!!)
        }
        type!!.name = classType!!.replace('/', '.')
    }

    override fun visitInnerClassType(innerClassType: String?) {
        if (trace) {
            LOG.trace("JavadocSignatureVisitor.visitInnerClassType($innerClassType)")
        }
    }

    override fun visitTypeArgument() {
        push("JavadocSignatureVisitor.visitTypeArgument")
    }

    private fun push(item: String) {
        stack.push(item)
        if (trace) {
            LOG.trace("push stack = " + stack)
        }
    }

    private fun pop(): String {
        val pop = stack.pop()
        if (trace) {
            LOG.trace("pop stack = " + stack)
        }
        return pop
    }

    override fun visitTypeArgument(typeArgument: Char): SignatureVisitor {
        if (trace) {
            LOG.trace("JavadocSignatureVisitor.visitTypeArgument($typeArgument)")
        }
        return this
    }

    override fun visitEnd() {
        val stage = pop()
        if (trace) {
            LOG.trace("---- JavadocSignatureVisitor.visitEnd : " + stage)
        }
    }

    override fun toString(): String {
        val sb = StringBuilder("JavadocSignatureVisitor{")
        sb.append("className='").append(className).append('\'')
        sb.append(", methodName='").append(name).append('\'')
        sb.append(", signature='").append(signature).append('\'')
        sb.append('}')
        return sb.toString()
    }

    public fun getTypes(): List<JavadocType> {
        if (varargs && !types.isEmpty()) {
            types.get(types.size() - 1).setVarargs(true)
        }
        return types
    }

    public class JavadocType {
        public var name: String? = null
            set(value) {
                if ($name == null) {
                    $name = value
                } else if(value != null){
                    typeVariables.add(value)
                }
            }

        private val typeVariables = ArrayList<String>()

        private var array: Boolean = false

        private var varargs: Boolean = false

        public constructor() {
        }

        public constructor(s: String) {
            name = s
        }

        public fun addTypeVariable(typeVariable: String) {
            typeVariables.add(typeVariable)
        }

        public fun setArray(array: Boolean) {
            this.array = array
        }

        public fun isArray(): Boolean {
            return array
        }

        override fun toString(): String {
            if (name == null && typeVariables.size() == 1) {
                return typeVariables.get(0)
            }
            val builder: StringBuilder
            try {
                builder = StringBuilder(name)
            } catch (e: NullPointerException) {
                throw RuntimeException(e.getMessage(), e)
            }

            val generics = StringBuilder()
            for (typeVariable in typeVariables) {
                if (generics.length() != 0) {
                    generics.append(", ")
                }
                generics.append(JavadocClassVisitor.calculateNameAndPackage(typeVariable)[1])
            }
            if (generics.length() != 0) {
                builder.append("<").append(generics).append(">")
            }
            if (isArray()) {
                if (varargs) {
                    builder.append("...")
                } else {
                    builder.append("[]")
                }
            }
            return builder.toString()
        }

        public fun setVarargs(varargs: Boolean) {
            this.varargs = varargs
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(JavadocSignatureVisitor::class.java)

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
    }
}