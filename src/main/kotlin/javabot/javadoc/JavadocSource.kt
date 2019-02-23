package javabot.javadoc

import javabot.dao.JavadocClassDao
import javabot.model.javadoc.JavadocApi
import javabot.model.javadoc.JavadocClass
import javabot.model.javadoc.JavadocMethod
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.IndexOptions
import org.mongodb.morphia.annotations.Indexes
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URLDecoder
import java.util.Date

abstract class JavadocSource() {
    companion object {
        val LOG = LoggerFactory.getLogger(JavadocSource::class.java)
    }

    constructor(api: JavadocApi, file: String) : this() {
        this.api = api
        this.name = file
    }

    lateinit var name: String

    lateinit var api: JavadocApi
    var created: Date = Date()
    protected var typeParameters: List<String> = listOf()

    fun parse(javadocClassDao: JavadocClassDao) {
        val document = Jsoup.parse(File(name), "UTF-8")
        val docClass = JavadocClass(api, moduleName(document), packageName(document), className(document))
        if (docClass.packageName.isEmpty() or docClass.name.isEmpty()) {
            throw IllegalStateException("package or class name is null in $name")
        }

        javadocClassDao.save(docClass)
        updateType(document, docClass)

//                docClass.visibility = when {
//                    source.getEnclosingType().isInterface() -> Public
//                    else -> visibility(source.getVisibility().scope())
//                }
        discoverGenerics(document, docClass)
        discoverParentType(document, docClass)
        discoverInterfaces(document, docClass)
        discoverMembers(javadocClassDao, document, docClass)

//                nestedTypes(api, packages, source)
        javadocClassDao.save(docClass)
    }

    fun stripPackage(name: String): String {
        return if (name.endsWith("...")) {
            stripPackage(name.dropLast(3)) + "..."
        } else {
            if ("." in name) name.split(".").dropWhile { !it[0].isUpperCase() }.joinToString(".")
            else name
        }
    }

    protected fun extractMethod(klass: JavadocClass, name: String, href: String): JavadocMethod {
        val (longArgs, shortArgs) = extractParameters(href)
        return JavadocMethod(klass, name, href, longArgs, shortArgs)
    }

    open fun extractParameters(href: String): Pair<List<String>, List<String>> {
        val longArgs = mutableListOf<String>()
        val shortArgs = mutableListOf<String>()
        val paramText = URLDecoder.decode(href, "UTF-8")
            .substringAfter("(")
            .substringBefore(")")
        if (paramText != "") {
            paramText.split(",").forEach {
                val param = if (it in typeParameters) "java.lang.Object" else it
                longArgs.add(param)
                shortArgs.add(stripPackage(param))
            }
        }
        return longArgs to shortArgs
    }

    fun updateType(document: Document, docClass: JavadocClass) {
        val type = findType(document)
        when (type) {
            "class" -> docClass.isClass = true
            "annotation" -> docClass.isAnnotation = true
            "interface" -> docClass.isInterface = true
            "enum" -> docClass.isEnum = true
            else -> TODO("unknown type: $type")
        }
    }

    protected fun extractPackageFromElement(a: Element) = a.attr("title").substringAfterLast(" ")

    protected open fun moduleName(document: Document): String? = null

    protected abstract fun className(document: Document): String
    protected abstract fun packageName(document: Document): String

    protected abstract fun discoverGenerics(document: Document, klass: JavadocClass)
    protected abstract fun discoverMembers(javadocClassDao: JavadocClassDao, document: Document, docClass: JavadocClass)
    protected abstract fun discoverParentType(document: Document, klass: JavadocClass)

    protected abstract fun discoverInterfaces(document: Document, klass: JavadocClass)

    protected abstract fun findType(document: Document): String?
}
