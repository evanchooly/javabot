package javabot.javadoc

import javabot.dao.JavadocClassDao
import javabot.model.javadoc.JavadocApi
import javabot.model.javadoc.JavadocClass
import javabot.model.javadoc.JavadocField
import org.jsoup.nodes.Document

class Java8JavadocSource() : JavadocSource() {
    constructor(api: JavadocApi, file: String) : this() {
        this.api = api
        this.name = file
    }

    override fun findType(document: Document): String? {
        return document.select("h2.title").text().substringBefore(" ").toLowerCase()
    }

    override fun className(document: Document): String {
        val text = document.select("h2.title").text().substringAfter(" ")
        typeParameters = if("<" in text) {
            text.substringAfter("<").substringBefore(">")
                    .split(",")
                    .map { it.trim() }
        } else listOf()
        return text.substringBefore("<")
    }

    override fun packageName(document: Document): String {
        return document.select("""div.header > div.subtitle""").text()
    }

    override fun discoverGenerics(document: Document, klass: JavadocClass) {
        TODO("not implemented")
    }

    override fun discoverMembers(javadocClassDao: JavadocClassDao, document: Document, docClass: JavadocClass) {
        document.select("span.memberNameLink")
                .map {
                    val link = it.child(0)
                    val name = link.text()
                    val href = link.attr("href")
                    javadocClassDao.save(
                            if ("-" in href) extractMethod(docClass, name, href)
                            else JavadocField(docClass, name, href)
                    )
                }
    }

    override fun extractParameters(href: String): Pair<List<String>, List<String>> {
        val longArgs = mutableListOf<String>()
        val shortArgs = mutableListOf<String>()
        val paramText = href.substringAfter("-").substringBeforeLast("-")
        if (paramText != "") {
            paramText.split("-").forEach {
                val param = if (it in typeParameters) "java.lang.Object" else it
                longArgs.add(param)
                shortArgs.add(stripPackage(param))
            }
        }
        return longArgs to shortArgs
    }

    override fun discoverParentType(document: Document, klass: JavadocClass) {
        val select = document.select("ul.inheritance")
        if (select.size != 0) {
            val last = select.dropLast(1)
            if (!last.isEmpty()) {
                klass.parentTypes += last.last().child(0).text()
            }
        }
    }

    override fun discoverInterfaces(document: Document, klass: JavadocClass) {
        document.select("dt")
                .firstOrNull { it.text().trim() == "All Implemented Interfaces:" }
                ?.let {
                    klass.parentTypes += it.nextElementSibling()
                            .children()
                            .map { element ->
                                element.selectFirst("a")
                            }.map { a ->
                                "${extractPackageFromElement(a)}.${a.text()}"
                            }
                }
    }

    override fun toString(): String {
        return "Java11JavadocSource(api=${api.name}, name='$name')"
    }
}
