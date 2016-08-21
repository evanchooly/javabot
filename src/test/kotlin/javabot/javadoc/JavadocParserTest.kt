package javabot.javadoc

import javabot.BaseTest
import javabot.JavabotConfig
import javabot.model.ApiEvent
import javabot.model.downloadZip
import org.testng.Assert
import org.testng.annotations.Test
import java.io.File
import java.io.OutputStreamWriter
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
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
        parser.buildHtml(JavadocApi("JDK", "${config.url()}/javadoc/JDK", "", "", ""), file, listOf("java", "javax"))
        val host = config.databaseHost()
        val port = config.databasePort()
        val database = config.databaseName()

        val uri = URI("gridfs://$host:$port/$database.javadoc/JDK/java/applet/Applet.html")
        Assert.assertTrue(Files.exists(Paths.get(uri)))
    }
}