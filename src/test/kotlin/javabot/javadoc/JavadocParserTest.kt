package javabot.javadoc

import javabot.BaseTest
import javabot.JavabotConfig
import javabot.admin.JavadocTest
import javabot.dao.ApiDao
import javabot.dao.JavadocClassDao
import javabot.javadoc.JavadocType.JAVA11
import javabot.javadoc.JavadocType.JAVA6
import javabot.javadoc.JavadocType.JAVA7
import javabot.javadoc.JavadocType.JAVA8
import javabot.model.javadoc.JavadocApi
import org.testng.Assert
import org.testng.annotations.Test
import java.io.File
import java.io.StringWriter
import javax.inject.Inject

class JavadocParserTest : BaseTest() {
    @Inject
    lateinit var parser: JavadocParser
    @Inject
    lateinit var config: JavabotConfig
    @Inject
    lateinit var apiDao: ApiDao
    @Inject
    lateinit var javadocClassDao: JavadocClassDao

    @Test
    fun testBuildHtml() {
        parser.extractJavadocContent(JavadocApi(config, "JDK"))

        Assert.assertTrue(File("javadoc/JDK/11/java/applet/Applet.html").exists())
        Assert.assertTrue(File("javadoc/JDK/11/java/util/Map.Entry.html").exists())

        Assert.assertTrue(File("javadoc/JDK/11/index.html").readText()
                .contains("top.classFrame.location = top.targetPage + location.hash;"))
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

        val javadocDir = parser.extractJavadocContent(api)
        val type = JavadocType.discover(javadocDir)
        Assert.assertEquals(type, JAVA11)
        type.create(api, File(javadocDir, "java.base/java/util/Map.html").absolutePath)
                .parse(javadocClassDao)
    }

    @Test
    fun targetedServlet() {
        val apiName = "Servlet"
        apiDao.delete(apiName)

        val api = JavadocApi(config, apiName, "javax.servlet", "javax.servlet-api", JavadocTest.servletVersion)
        datastore.save(api)

        val javadocDir = parser.extractJavadocContent(api)
        val type = JavadocType.discover(javadocDir)
        Assert.assertEquals(type, JAVA6)
        type.create(api, File(javadocDir, "javax/servlet/http/Cookie.html").absolutePath)
                .parse(javadocClassDao)
    }

    @Test
    fun targetedJavaEE() {
        val apiName = "JavaEE7"
        apiDao.delete(apiName)

        val api = JavadocApi(config, apiName, "javax", "javaee-api", "7.0")
        datastore.save(api)

        val javadocDir = parser.extractJavadocContent(api)
        val type = JavadocType.discover(javadocDir)
        Assert.assertEquals(type, JAVA7)
        type.create(api, File(javadocDir, "javax/enterprise/concurrent/ContextService.html").absolutePath)
            .parse(javadocClassDao)
        type.create(api, File(javadocDir, "javax/ejb/AsyncResult.html").absolutePath)
            .parse(javadocClassDao)
        type.create(api, File(javadocDir, "javax/persistence/Cache.html").absolutePath)
            .parse(javadocClassDao)
        type.create(api, File(javadocDir, "javax/jws/soap/InitParam.html").absolutePath)
                .parse(javadocClassDao)
    }

    @Test
    fun targetedGuava() {
        val apiName = "guava"
        apiDao.delete(apiName)

        val api = JavadocApi(config, apiName, "com.google.guava", "guava", "19.0")
        datastore.save(api)

        val javadocDir = parser.extractJavadocContent(api)
        val type = JavadocType.discover(javadocDir)
        Assert.assertEquals(type, JAVA8)
        type.create(api, File(javadocDir, "com/google/common/escape/Escapers.Builder.html").absolutePath)
                .parse(javadocClassDao)
    }
}
