package javabot.javadoc

import com.mongodb.MongoException
import javabot.dao.ApiDao
import javabot.dao.JavadocClassDao
import javabot.model.javadoc.JavadocApi
import javabot.model.javadoc.JavadocClass
import javabot.model.javadoc.JavadocField
import javabot.model.javadoc.JavadocMethod
import javabot.model.javadoc.Visibility.Public
import org.jboss.forge.roaster.model.Extendable
import org.jboss.forge.roaster.model.Field
import org.jboss.forge.roaster.model.FieldHolder
import org.jboss.forge.roaster.model.InterfaceCapable
import org.jboss.forge.roaster.model.JavaType
import org.jboss.forge.roaster.model.Method
import org.jboss.forge.roaster.model.MethodHolder
import org.jboss.forge.roaster.model.Parameter
import org.jboss.forge.roaster.model.TypeHolder
import org.jboss.forge.roaster.model.source.Importer
import org.jboss.forge.roaster.model.source.JavaSource
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import java.util.ArrayList
import java.util.HashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JavadocClassParser @Inject constructor(var javadocClassDao: JavadocClassDao, var apiDao: ApiDao, var parser: JavadocParser) {

    companion object {
        private val PRIMITIVES = HashMap<Char, String>()
        private val LOG = LoggerFactory.getLogger(JavadocClassParser::class.java)

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

            return pkgName to clsName
        }
    }

    fun parse(api: JavadocApi, source: JavaType<*>, vararg packages: String) {
        val pkg = source.getPackage()
        var process = packages.isEmpty()
        for (aPackage in packages) {
            process = process || pkg.startsWith(aPackage)
        }
        if (process) {
            val className = source.getCanonicalName().replace(pkg + ".", "")
            val docClass = JavadocClass(api, pkg, className)
            try {
                javadocClassDao.save(docClass)

                docClass.isClass = source.isClass()
                docClass.isAnnotation = source.isAnnotation()
                docClass.isInterface = source.isInterface()
                docClass.isEnum = source.isEnum()
                docClass.visibility = when {
                    source.getEnclosingType().isInterface() -> Public
                    else -> visibility(source.getVisibility().scope())
                }
//            generics(docClass, source)
                extendable(docClass, source)
                interfaces(docClass, source)
                methods(docClass, source)
                fields(docClass, source)
                nestedTypes(api, packages, source)
            } catch (e: MongoException) {
                LOG.error(e.message, e)
            }
        }
    }

    fun parse(api: JavadocApi, document: Document) {
        val pkg = document.select("""div.subTitle a[href="package-summary.html"]""").text()
        println("pkg = ${pkg}")
        val className = document.select("span.typeNameLabel").text()
        val docClass = JavadocClass(api, pkg, className)

        try {
            javadocClassDao.save(docClass)
            val type = document.select("h2.title").text().substringBefore(" ").toLowerCase()
            when (type) {
                "class" -> docClass.isClass = true
                "annotation" -> docClass.isAnnotation = true
                "interface" -> docClass.isInterface = true
                "enum" -> docClass.isEnum = true
                else -> TODO("unknown type: $type")
            }

//                docClass.visibility = when {
//                    source.getEnclosingType().isInterface() -> Public
//                    else -> visibility(source.getVisibility().scope())
//                }
//            generics(docClass, source)
            extendable(docClass, document)
            interfaces(docClass, document)
//                methods(docClass, source)
//                fields(docClass, source)
//                nestedTypes(api, packages, source)
        } catch (e: MongoException) {
            LOG.error(e.message, e)
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
                    .forEach { visitField(klass, it) }
            javadocClassDao.save(klass)
        }
    }

    private fun methods(klass: JavadocClass, source: JavaType<*>) {
        if (source is MethodHolder<*>) {
            source.getMethods()
                    .forEach { parseMethod(klass, it) }
        }
    }

    private fun interfaces(klass: JavadocClass, source: JavaType<*>) {
        if (source is InterfaceCapable) {
            source as Importer<*>
            source.interfaces
                    .map { source.resolveType(it) }
                    .forEach { klass.interfaces.add(it) }

        }
    }

    private fun extendable(klass: JavadocClass, source: JavaType<*>, retry: Int = 0) {
        if (source is Extendable<*>) {
            try {
                klass.parentClass = parser.getJavadocClass(source.getSuperType())?.fqcn
            } catch (e: Exception) {
                if (retry < 10) {
                    extendable(klass, source, retry + 1)
                } else {
                    println(e.message + "\nsource.getQualifiedName() = ${source.getQualifiedName()}")
                }
            }
        }
    }

    private fun extendable(klass: JavadocClass, document: Document) {
        klass.parentClass = document
            .select("ul.inheritance")
            .dropLast(1)
            .last()
            .children().first()
            .children().first().text()
    }

    private fun interfaces(klass: JavadocClass, document: Document) {
        val interfaces = document.select("dt")
            .filter { it.text().trim() == "All Implemented Interfaces:" }
            .firstOrNull()
        interfaces?.let {
            val list = it.nextElementSibling().children().map {
                    it.selectFirst("a")
                }.map {
                    val pkg = it.attr("title").substringAfterLast(" ")
                    "${pkg}.${it.text()}"
                }.toMutableList()
            klass.interfaces.addAll(list)
        }
    }

    /*
    private fun generics(klass: JavadocClass, source: JavaType<*>) {
        if ( source is GenericCapable<*>) {
            source.getTypeVariables()
                    .map { JavadocType(it) }
//                    .forEach { klass.typeVariables.add(it) }
        }
    }
*/

    fun visitField(javadocClass: JavadocClass, field: Field<*>): JavadocField {
        val entity = JavadocField(javadocClass, field.getName(), field.getType().toString())
        entity.visibility = visibility(field.getVisibility().scope())
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
        entity.visibility = visibility(method.getVisibility().scope())

        javadocClassDao.save(entity)
        return entity
    }

    private fun update(longTypes: MutableList<String>, shortTypes: MutableList<String>, arg: Parameter<out JavaType<*>>) {
        try {
            val origin = arg.getOrigin() as JavaSource
            var value: String
            if (origin.getImport(arg.getType().getSimpleName()) != null) {
                value = arg.getType().getQualifiedName()
            } else {
                value = if (arg.getType().getQualifiedName().startsWith("java.lang"))
                    arg.getType().getQualifiedName()
                else
                    arg.getType().getName()
            }
            if (arg.isVarArgs()) {
                value += "..."
            }
            if (arg.getType().isArray() && !value.endsWith("[]")) {
                value += "[]"
            }
            longTypes.add(value)
            shortTypes.add(calculateNameAndPackage(value).second)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }
}
