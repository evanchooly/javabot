package javabot.javadoc

import javabot.BaseTest
import javabot.JavabotConfig
import javabot.model.ApiEvent
import javabot.model.downloadZip
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
        val downloadUrl = ApiEvent.locateJDK()
        val file = downloadUrl.downloadZip()
        parser.buildHtml(JavadocApi("JDK", "${config.url()}/javadoc/JDK", "", "", ""), file, listOf("java", "javax"))

        val applet = File("javadoc/JDK/1.8/java/applet/Applet.html")
        Assert.assertTrue(applet.exists())

        Assert.assertTrue(File("javadoc/JDK/1.8/index.html").readText()
                .contains("top.classFrame.location = top.targetPage + location.hash;"))
    }
}