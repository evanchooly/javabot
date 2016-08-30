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
        if (process && !source.isPrivate()) {
            val className = source.getCanonicalName().replace(source.getPackage() + ".", "")
            val klass = parser.getJavadocClass(api, pkg, className)
            klass.isClass = source.isClass()
            klass.isAnnotation = source.isAnnotation()
            klass.isInterface = source.isInterface()
            klass.isEnum = source.isEnum()
            generics(klass, source)
            extendable(api, klass, source)
            interfaces(api, klass, source)
            methods(klass, source)
            fields(klass, source)
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
                    .map { visitField(klass, it) }
                    .filterNotNull()
                    .forEach { klass.fields.add(it) }
            javadocClassDao.save(klass)
        }
    }

    private fun methods(klass: JavadocClass, source: JavaType<*>) {
        if (source is MethodHolder<*>) {
            source.getMethods()
                    .map { parseMethod(klass, it) }
                    .filterNotNull()
                    .forEach { klass.methods.add(it) }
        }
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
        if ( source is Extendable<*>) {
            klass.parentClass = parser.getJavadocClass(api, source.getSuperType())
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

    fun visitField(javadocClass: JavadocClass, field: Field<*>): JavadocField? {
        var entity: JavadocField? = null
        if (!(field.isPrivate() && field.isPackagePrivate())) {
            entity = JavadocField(javadocClass, field.getName(), field.getType().toString())
            javadocClassDao.save(entity)
        }
        return entity
    }

    fun parseMethod(klass: JavadocClass, method: Method<*, *>): JavadocMethod? {
        var entity: JavadocMethod? = null
        if (!(method.isPrivate() && method.isPackagePrivate())) {
            val types = method.getParameters()

            val longTypes = ArrayList<String>()
            val shortTypes = ArrayList<String>()
            types.forEach { update(longTypes, shortTypes, it) }
            val methodName = method.getName()
            entity = JavadocMethod(klass, methodName, types.size, longTypes, shortTypes)
            javadocClassDao.save(entity)
        }
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