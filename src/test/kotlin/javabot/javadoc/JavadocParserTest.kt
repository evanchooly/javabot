package javabot.javadoc

import javabot.BaseTest
import javabot.JavabotConfig
import javabot.admin.JavadocTest
import javabot.dao.ApiDao
import javabot.dao.JavadocClassDao
import javabot.model.javadoc.Java6JavadocSource
import javabot.model.javadoc.Java8JavadocSource
import javabot.model.javadoc.JavadocApi
import org.jsoup.Jsoup
import org.testng.Assert
import org.testng.annotations.Test
import java.io.File
import java.io.StringWriter
import javax.inject.Inject
import javax.inject.Provider

class JavadocParserTest : BaseTest() {
    @Inject
    lateinit var parser: JavadocParser
    @Inject
    lateinit var config: JavabotConfig
    @Inject
    lateinit var apiDao: ApiDao
    @Inject
    lateinit var provider: Provider<JavadocClassParser>
    @Inject
    lateinit var javadocClassDao: JavadocClassDao

    @Test
    fun testBuildHtml() {
        parser.extractJavadocContent(JavadocApi(config, "JDK"), File(config.jdkJavadoc()))

        Assert.assertTrue(File("javadoc/JDK/1.8/java/applet/Applet.html").exists())
        Assert.assertTrue(File("javadoc/JDK/1.8/java/util/Map.Entry.html").exists())

        Assert.assertTrue(File("javadoc/JDK/1.8/index.html").readText()
                .contains("top.classFrame.location = top.targetPage + location.hash;"))
    }

    @Test
    fun packages() {
        Assert.assertEquals(JavadocParser.getPackage("java.util.List"), Pair("java.util", "List"))
        Assert.assertEquals(JavadocParser.getPackage("java.util.Map"), Pair("java.util", "Map"))
        Assert.assertEquals(JavadocParser.getPackage("java.util.Map.Entry"), Pair("java.util", "Map.Entry"))
    }

    @Test
    fun jdk() {
        val api = JavadocApi(config, "JDK")
        apiDao.find("JDK")?.let { apiDao.delete(it) }
        datastore.save(api)
        parser.parse(api, SOut)
    }

    object SOut : StringWriter() {
        override fun write(line: String) = println(line)
    }

    @Test
    fun targeted() {
        val apiName = "JDK"
        apiDao.delete(apiName)

        val api = JavadocApi(config, apiName)
        datastore.save(api)

        val javadocDir = parser.extractJavadocContent(api, File(config.jdkJavadoc()))
        val source = Java8JavadocSource(api, File(javadocDir, "java.desktop/java/awt/Dimension.html").absolutePath)
        source.parse(javadocClassDao)
    }

    @Test
    fun targetedServlet() {
        val apiName = "Servlet"
        apiDao.delete(apiName)

        val api = JavadocApi(config, apiName, "javax.servlet", "javax.servlet-api", JavadocTest.servletVersion)
        datastore.save(api)

        val javadocDir = parser.extractJavadocContent(api, File(config.jdkJavadoc()))
        val source = Java6JavadocSource(api, File(javadocDir, "java.desktop/java/awt/Dimension.html").absolutePath)
        source.parse(javadocClassDao)
    }
}
