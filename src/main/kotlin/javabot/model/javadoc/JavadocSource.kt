package javabot.model.javadoc

import javabot.javadoc.JavadocClassParser
import javabot.model.Persistent
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.IndexOptions
import org.mongodb.morphia.annotations.Indexes
import org.mongodb.morphia.annotations.Reference
import java.io.File
import java.util.Date

@Indexes(
        Index(fields = arrayOf(Field("created")), options = IndexOptions(expireAfterSeconds = 7200))
)
class JavadocSource() : Persistent {
    constructor(api: JavadocApi, file: String) : this() {
        this.api = api
        this.name = file
    }

    lateinit var name: String

    @Reference(idOnly = true)
    lateinit var api: JavadocApi
    var created: Date = Date()
    var processed: Boolean = false

    fun process(parser: JavadocClassParser) {
        val document = Jsoup.parse(File(name).readText())
        val attribute = findType(document)

        if (attribute != null) {
            val (name, type) = attribute.split(" ")
            if (!name.startsWith("jdk.")) {
                when (type) {
                    "class" -> {
                        parser.parse(api, document)
                    }
                    "interface" -> {
                        parser.parse(api, document)
                    }
                    else -> TODO("handle $type in $name")
                }
            }
        }
    }

    private fun findType(document: Document): String? {
        return document.getElementsByTag("meta")
                .flatMap { it.attributes() }
                .filter { it.key == "content" && it.value.contains(" ") && !it.value.contains("/") }
                .map { it.value }
                .firstOrNull()
    }

    override fun toString(): String {
        return "JavadocSource(api=${api.name}, name='$name')"
    }

}
