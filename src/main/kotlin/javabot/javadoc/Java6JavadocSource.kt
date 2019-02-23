package javabot.javadoc

import javabot.dao.JavadocClassDao
import javabot.model.javadoc.JavadocApi
import javabot.model.javadoc.JavadocClass
import javabot.model.javadoc.JavadocField
import org.jsoup.nodes.Document

class Java6JavadocSource(api: JavadocApi, file: String) : JavadocSource(api, file) {
    lateinit var packageName: String
    lateinit var type: String

    override fun className(document: Document): String {
        var text = document.select("h2").text()
        text = text.removePrefix(packageName).trim()
        type = text.substringBefore(" ").toLowerCase()
        text = text.substringAfter(" ").trim()

        return text
    }

    override fun packageName(document: Document): String {
        packageName = document.select("h2 > font").text().trim()
        return packageName
    }

    override fun findType(document: Document): String {
        return type
    }

    override fun discoverMembers(javadocClassDao: JavadocClassDao, document: Document, docClass: JavadocClass) {
        val list = document.select("a[name$=)]")
                .map { it.attr("name") }
        list.forEach {
            val name = it.substringBefore("(")
            val href = "#$it"
            javadocClassDao.save(
                    if ("(" in href) extractMethod(docClass, name, href)
                    else JavadocField(docClass, name, href)
            )

        }
    }

    override fun discoverParentType(document: Document, klass: JavadocClass) {
        val select = document.select("dl > dt > pre").select("dt")
        select.forEach { element ->
            val a = element.select("a").first()
            klass.parentTypes.add("${extractPackageFromElement(a)}.${a.text()}")
        }
    }

    override fun discoverInterfaces(document: Document, klass: JavadocClass) {
    }
}
