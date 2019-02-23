package javabot.javadoc

import javabot.dao.JavadocClassDao
import javabot.model.javadoc.JavadocApi
import javabot.model.javadoc.JavadocClass
import javabot.model.javadoc.JavadocField
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class Java11JavadocSource() : JavadocSource() {
    constructor(api: JavadocApi, file: String) : this() {
        this.api = api
        this.name = file
    }

    override fun findType(document: Document): String? {
        return document.select("h2.title").text().substringBefore(" ").toLowerCase()
    }

    override fun className(document: Document): String {
        val text = document.select("h2.title").text().substringBefore("<").substringAfterLast(" ")
        typeParameters = if("<" in text) {
            text.substringAfter("<").substringBefore(">")
                    .split(",")
                    .map { it.trim() }
        } else listOf()
        return text.substringBefore("<")
    }

    override fun packageName(document: Document): String = document.select("""div.subTitle a[href="package-summary.html"]""").text()

    override fun discoverGenerics(document: Document, klass: JavadocClass) {
    }

    override fun discoverMembers(javadocClassDao: JavadocClassDao, document: Document, docClass: JavadocClass) {
        fun mapper(): (Element) -> Unit {
            return {
                val link = it.child(0)
                val name = link.text()
                val href = link.attr("href")
                javadocClassDao.save(
                        if ("(" in href) extractMethod(docClass, name, href)
                        else JavadocField(docClass, name, href)
                )
            }
        }

        document.select("span.memberNameLink").map(mapper())
    }

    override fun discoverParentType(document: Document, klass: JavadocClass) {
        klass.parentTypes += extractParentType(document)
            .drop(1)
    }

    private fun extractParentType(element: Element): List<String> {
        return element.selectFirst("ul.inheritance")?.let { parent: Element ->
            val li = parent.select("li")
            val drop = li.first()
            val others = if(li.size > 1) {
                extractParentType(li[1])
            } else listOf()
            others + drop.text().substringBefore("<")
        } ?: listOf()
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
                            }.toMutableList()
                }
    }

    override fun toString(): String {
        return "Java11JavadocSource(api=${api.name}, name='$name')"
    }
}
