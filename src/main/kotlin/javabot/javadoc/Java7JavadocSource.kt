package javabot.javadoc

import javabot.dao.JavadocClassDao
import javabot.model.javadoc.JavadocApi
import javabot.model.javadoc.JavadocClass
import javabot.model.javadoc.JavadocField
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.net.URLDecoder

class Java7JavadocSource() : JavadocSource() {
    constructor(api: JavadocApi, file: String) : this() {
        this.api = api
        this.name = file
    }

    override fun findType(document: Document): String? {
        return document.select("h2.title").text().substringBefore(" ").toLowerCase()
    }

    override fun className(document: Document): String {
        val text = document.select("h2.title").text().substringAfterLast(" ")
        typeParameters = if("<" in text) {
            text.substringAfter("<").substringBefore(">")
                    .split(",")
                    .map { it.trim() }
        } else listOf()
        return text.substringBefore("<").substringAfterLast(" ")
    }

    override fun packageName(document: Document): String {
        return document.select("""div.header > div.subtitle""").text()
    }

    override fun discoverGenerics(document: Document, klass: JavadocClass) {
    }

    override fun discoverMembers(javadocClassDao: JavadocClassDao, document: Document, docClass: JavadocClass) {
        fun memberMapper(element: Element) {
            val link = element.selectFirst("a")
            val name = link.text()
            val href = link.attr("href")
            javadocClassDao
                .save(if ("(" in href) extractMethod(docClass, name, href) else JavadocField(docClass, name, href))
        }

        document.select("table[class=overviewSummary]").select("td[class=colOne]")
            .map { memberMapper(it) }

        document.select("table[class=overviewSummary]").select("td[class=colLast]")
            .map { memberMapper(it) }
    }

    override fun extractParameters(href: String): Pair<List<String>, List<String>> {
        val longArgs = mutableListOf<String>()
        val shortArgs = mutableListOf<String>()
        val paramText =  URLDecoder.decode(href, "UTF-8")
            .substringAfter("(")
            .substringBeforeLast(")")
        if (paramText != "") {
            paramText.split(", ").forEach {
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
