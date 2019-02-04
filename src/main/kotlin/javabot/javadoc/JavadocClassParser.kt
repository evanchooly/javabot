package javabot.javadoc

import com.mongodb.MongoException
import javabot.dao.ApiDao
import javabot.dao.JavadocClassDao
import javabot.model.javadoc.JavadocApi
import javabot.model.javadoc.JavadocClass
import javabot.model.javadoc.JavadocField
import javabot.model.javadoc.JavadocMethod
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JavadocClassParser @Inject constructor(private var javadocClassDao: JavadocClassDao, var apiDao: ApiDao) {

    companion object {
        private val LOG = LoggerFactory.getLogger(JavadocClassParser::class.java)

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

/*
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
*/

    fun parse(api: JavadocApi, document: Document) {
        val pkg = document.select("""div.subTitle a[href="package-summary.html"]""").text()
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

//                nestedTypes(api, packages, source)
        } catch (e: MongoException) {
            LOG.error(e.message, e)
        }
    }

    private fun extendable(klass: JavadocClass, document: Document) {
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

    private fun interfaces(klass: JavadocClass, document: Document) {
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

    private fun extractMethod(klass: JavadocClass, name: String, href: String): JavadocMethod {
        val (longArgs, shortArgs) = extractParameters(href)
        return JavadocMethod(klass, name, href, longArgs, shortArgs)
    }

    private fun extractParameters(href: String): Pair<List<String>, List<String>> {
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
}
