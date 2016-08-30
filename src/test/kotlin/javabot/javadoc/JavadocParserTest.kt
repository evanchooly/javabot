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
        val file = downloadUrl.downloadZip(File("/tmp/JDK.jar"))
        // on windows this seems to be in javadoc/JDK/1.8 ---- MAY BE DIFFERENT ON DIFFERENT OSES.
        // if so, we need to detect the 'problem' and remove the 1.8/ if detected.
        val appletFilename = "javadoc/JDK/1.8/java/applet/Applet.html"
        var applet = File(appletFilename)
        if (!applet.exists()) {
            parser.buildHtml(JavadocApi("JDK", "${config.url()}/javadoc/JDK/1.8", "", "", ""), file, listOf("java", "javax"))
        }
        // not sure if this resets without instantiation or not.
        applet = File(appletFilename)
        Assert.assertTrue(applet.exists())
    }
}