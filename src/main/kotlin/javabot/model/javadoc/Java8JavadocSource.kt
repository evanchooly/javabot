package javabot.model.javadoc

import javabot.dao.JavadocClassDao
import javabot.javadoc.JavadocClassParser
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File

class Java8JavadocSource() : JavadocSource() {
    constructor(api: JavadocApi, file: String) : this() {
        this.api = api
        this.name = file
    }

    override fun process(dao: JavadocClassDao, parser: JavadocClassParser) {
        val document = Jsoup.parse(File(name).readText())
        val attribute = findType(document)

        if (attribute != null) {
            val (name, type) = attribute.split(" ")
            if (!name.startsWith("jdk.")) {
                when (type) {
                    "class" -> {
                        parse(dao)
                    }
                    "interface" -> {
                        parse(dao)
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

    override fun className(document: Document): String = document.select("span.typeNameLabel").text()

    override fun packageName(document: Document): String = document.select("""div.subTitle a[href="package-summary.html"]""").text()

    override fun updateType(document: Document, docClass: JavadocClass) {
        val type = document.select("h2.title").text().substringBefore(" ").toLowerCase()
        when (type) {
            "class" -> docClass.isClass = true
            "annotation" -> docClass.isAnnotation = true
            "interface" -> docClass.isInterface = true
            "enum" -> docClass.isEnum = true
            else -> TODO("unknown type: $type")
        }
    }

    override fun discoverMembers(javadocClassDao: JavadocClassDao, document: Document, docClass: JavadocClass) {
        document.select("span.memberNameLink")
                .map {
                    val link = it.child(0)
                    val name = link.text()
                    val href = link.attr("href")
                    javadocClassDao.save(
                            if ("(" in href) extractMethod(docClass, name, href)
                            else JavadocField(docClass, name, href)
                    )
                }
    }

    override fun discoverParentType(document: Document, klass: JavadocClass) {
        val select = document.select("ul.inheritance")
        if (select.size != 0) {
            val last = select.dropLast(1)
            if (!last.isEmpty()) {
                klass.parentClass = last
                        .last()
                        .children().first()
                        .children().first()
                        .text()
            }
        }
    }

    override fun discoverInterfaces(document: Document, klass: JavadocClass) {
        document.select("dt")
                .firstOrNull { it.text().trim() == "All Implemented Interfaces:" }
                ?.let {
                    val list = it.nextElementSibling()
                            .children()
                            .map { element ->
                                element.selectFirst("a")
                            }.map { a ->
                                val pkg = a.attr("title").substringAfterLast(" ")
                                "${pkg}.${a.text()}"
                            }.toMutableList()
                    klass.interfaces.addAll(list)
                }
    }

    override fun toString(): String {
        return "Java8JavadocSource(api=${api.name}, name='$name')"
    }
}