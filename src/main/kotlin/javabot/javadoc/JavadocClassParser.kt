package javabot.javadoc

import javabot.dao.ApiDao
import javabot.dao.JavadocClassDao
import org.jboss.forge.roaster.model.Extendable
import org.jboss.forge.roaster.model.Field
import org.jboss.forge.roaster.model.FieldHolder
import org.jboss.forge.roaster.model.GenericCapable
import org.jboss.forge.roaster.model.InterfaceCapable
import org.jboss.forge.roaster.model.JavaType
import org.jboss.forge.roaster.model.Method
import org.jboss.forge.roaster.model.MethodHolder
import org.jboss.forge.roaster.model.Parameter
import org.jboss.forge.roaster.model.TypeHolder
import org.jboss.forge.roaster.model.VisibilityScoped
import org.jboss.forge.roaster.model.source.JavaSource
import java.util.ArrayList
import java.util.HashMap
import javax.inject.Inject

class JavadocClassParser @Inject constructor(var javadocClassDao: JavadocClassDao, var apiDao: ApiDao, var parser: JavadocParser) {

    companion object {
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

        fun calculateNameAndPackage(value: String): Pair<String?, String> {
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
    }

    fun parse(api: JavadocApi, source: JavaType<*>, vararg packages: String) {
        val pkg = source.getPackage()
        var process = packages.isEmpty()
        for (aPackage in packages) {
            process = process || pkg.startsWith(aPackage)
        }
        if (process && isVisibleEnough(source)) {
            val className = source.getCanonicalName().replace(source.getPackage() + ".", "")
            val docClass = parser.getJavadocClass(api, pkg, className)
            docClass.isClass = source.isClass()
            docClass.isAnnotation = source.isAnnotation()
            docClass.isInterface = source.isInterface()
            docClass.isEnum = source.isEnum()
            generics(docClass, source)
            extendable(api, docClass, source)
            interfaces(api, docClass, source)
            methods(docClass, source)
            fields(docClass, source)
            nestedTypes(api, packages, source)
        }
    }

    private fun nestedTypes(api: JavadocApi, packages: Array<out String>, source: JavaType<*>) {
        if (source is TypeHolder<*>) {
            source.getNestedTypes()
                    .forEach { parse(api, it, *packages) }
        }
    }

    private fun fields(klass: JavadocClass, source: JavaType<*>) {
        if (source is FieldHolder<*>) {
            source.getFields()
                    .filter { isVisibleEnough(it) }
                    .map { visitField(klass, it) }
                    .forEach { klass.fields.add(it) }
            javadocClassDao.save(klass)
        }
    }

    private fun methods(klass: JavadocClass, source: JavaType<*>) {
        if (source is MethodHolder<*>) {
            source.getMethods()
                    .filter { isVisibleEnough(it) }
                    .map { parseMethod(klass, it) }
                    .forEach { klass.methods.add(it) }
        }
    }

    private fun isVisibleEnough(it: VisibilityScoped): Boolean {
        return it.isPublic || it.isProtected || (it is JavaType<*> && it.isInterface() && it.getEnclosingType() != null)
    }

    private fun interfaces(api: JavadocApi, klass: JavadocClass, source: JavaType<*>) {
        if (source is InterfaceCapable) {
            source.interfaces
                    .map { if (it.contains('.')) it else "${source.getPackage()}.${it}" }
                    .map { parser.getJavadocClass(api, it) }
                    .forEach { klass.interfaces.add(it) }

        }
    }

    private fun extendable(api: JavadocApi, klass: JavadocClass, source: JavaType<*>) {
        try {
            if ( source is Extendable<*>) {
                klass.parentClass = parser.getJavadocClass(api, source.getSuperType())
            }
        } catch(e: IllegalStateException) {
            println("source = ${source}")
        }
    }

    private fun generics(klass: JavadocClass, source: JavaType<*>) {
        if ( source is GenericCapable<*>) {
            source.getTypeVariables()
                    //                    .flatMap { it.getBounds() }
                    .map { JavadocType(it) }
                    .forEach { klass.typeVariables.add(it) }
        }
    }

    fun visitField(javadocClass: JavadocClass, field: Field<*>): JavadocField {
        val entity = JavadocField(javadocClass, field.getName(), field.getType().toString())
        javadocClassDao.save(entity)
        return entity
    }

    fun parseMethod(klass: JavadocClass, method: Method<*, *>): JavadocMethod {
        val types = method.getParameters()

        val longTypes = ArrayList<String>()
        val shortTypes = ArrayList<String>()
        types.forEach { update(longTypes, shortTypes, it) }
        val methodName = method.getName()
        val entity = JavadocMethod(klass, methodName, types.size, longTypes, shortTypes)
        javadocClassDao.save(entity)
        return entity
    }

    private fun update(longTypes: MutableList<String>, shortTypes: MutableList<String>, arg: Parameter<out JavaType<*>>) {
        val origin = arg.getOrigin() as JavaSource
        var value:  String
        if(origin.getImport(arg.getType().getSimpleName()) != null) {
            value = arg.getType().getQualifiedName()
        } else {
            value = if (arg.getType().getQualifiedName().startsWith("java.lang"))
                arg.getType().getQualifiedName()
            else
                arg.getType().getName()
        }
        if(arg.isVarArgs()) {
            value += "..."
        }
        if(arg.getType().isArray() && !value.endsWith("[]")) {
            value += "[]"
        }
        longTypes.add(value)
        shortTypes.add(calculateNameAndPackage(value).second)
    }
}