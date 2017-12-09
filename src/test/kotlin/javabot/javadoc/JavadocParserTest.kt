package javabot.javadoc

import javabot.BaseTest
import javabot.JavabotConfig
import javabot.model.ApiEvent
import javabot.model.downloadZip
import javabot.model.javadoc.JavadocApi
import org.testng.Assert
import org.testng.annotations.Test
import java.io.File
import javax.inject.Inject

class JavadocParserTest : BaseTest() {
    @Inject
    lateinit var parser: JavadocParser
    @Inject
    lateinit var config: JavabotConfig

    @Test
    fun testBuildHtml() {
        val file = ApiEvent.locateJDK().downloadZip()
        parser.buildHtml(JavadocApi("JDK", "${config.url()}/javadoc/JDK"), file, listOf("java", "javax"))

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
}