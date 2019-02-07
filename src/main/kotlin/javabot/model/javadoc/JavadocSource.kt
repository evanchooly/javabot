package javabot.model.javadoc

import com.mongodb.MongoException
import javabot.dao.JavadocClassDao
import javabot.model.Persistent
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.IndexOptions
import org.mongodb.morphia.annotations.Indexes
import org.mongodb.morphia.annotations.Reference
import org.slf4j.LoggerFactory
import java.io.File
import java.util.Date

@Entity("JavadocSources")
@Indexes(Index(fields = arrayOf(Field("created")), options = IndexOptions(expireAfterSeconds = 7200)))
abstract class JavadocSource() : Persistent {
    companion object {
        val LOG = LoggerFactory.getLogger(JavadocSource::class.java)
    }

    constructor(api: JavadocApi, file: String) : this() {
        this.api = api
        this.name = file
    }

    @Id
    lateinit var name: String

    @Reference(idOnly = true)
    lateinit var api: JavadocApi
    var created: Date = Date()

    abstract fun process(dao: JavadocClassDao)

    protected fun extractParameters(href: String): Pair<List<String>, List<String>> {
        val longArgs = mutableListOf<String>()
        val shortArgs = mutableListOf<String>()
        val paramText = href.substringAfter("(").substringBefore(")")
        if (paramText != "") {
            paramText.split(",").forEach {
                longArgs.add(it)
                shortArgs.add(stripPackage(it))
            }
        }
        return longArgs to shortArgs
    }

    private fun stripPackage(name: String): String {
        return if (name.endsWith("...")) {
            stripPackage(name.dropLast(3)) + "..."
        } else {
            name.split(".")
                    .dropWhile { !it[0].isUpperCase() }
                    .joinToString(".")
        }
    }

    protected fun extractMethod(klass: JavadocClass, name: String, href: String): JavadocMethod {
        val (longArgs, shortArgs) = extractParameters(href)
        return JavadocMethod(klass, name, href, longArgs, shortArgs)
    }

    fun parse(javadocClassDao: JavadocClassDao) {
        val document = Jsoup.parse(File(name), "UTF-8")
        val docClass = JavadocClass(api, packageName(document), className(document))

        try {
            javadocClassDao.save(docClass)
            updateType(document, docClass)

//                docClass.visibility = when {
//                    source.getEnclosingType().isInterface() -> Public
//                    else -> visibility(source.getVisibility().scope())
//                }
//            generics(docClass, source)
            discoverParentType(document, docClass)
            discoverInterfaces(document, docClass)
            discoverMembers(javadocClassDao, document, docClass)

//                nestedTypes(api, packages, source)
            javadocClassDao.save(docClass)
        } catch (e: MongoException) {
            LOG.error(e.message, e)
        }
    }

    protected abstract fun className(document: Document): String
    protected abstract fun packageName(document: Document): String
    protected abstract fun updateType(document: Document, docClass: JavadocClass)
    protected abstract fun discoverMembers(javadocClassDao: JavadocClassDao, document: Document, docClass: JavadocClass)
    protected abstract fun discoverParentType(document: Document, klass: JavadocClass)
    protected abstract fun discoverInterfaces(document: Document, klass: JavadocClass)
}
