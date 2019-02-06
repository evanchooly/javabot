package javabot.javadoc

import javabot.BaseTest
import javabot.JavabotConfig
import javabot.dao.ApiDao
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
        parser.parse(api, object : StringWriter() {
            override fun write(line: String) = println(line)
        })
    }

    @Test
    fun targeted() {
        datastore.db.dropDatabase()

        val api = JavadocApi(config, "JDK")
        datastore.save(api)

        val javadocDir = parser.extractJavadocContent(api, File(config.jdkJavadoc()))
        provider.get().parse(api, Jsoup.parse(File(javadocDir, "java.desktop/java/awt/Dimension.html"), "UTF-8"))
    }
}
