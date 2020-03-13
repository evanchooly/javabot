package javabot.javadoc

import javabot.BaseTest
import javabot.JavabotConfig
import javabot.admin.JavadocTest
import javabot.dao.JavadocClassDao
import javabot.javadoc.JavadocType.JAVA11
import javabot.javadoc.JavadocType.JAVA6
import javabot.javadoc.JavadocType.JAVA7
import javabot.javadoc.JavadocType.JAVA8
import javabot.model.javadoc.JavadocApi
import org.testng.Assert
import org.testng.annotations.Test
import java.io.File
import javax.inject.Inject

class JavadocParserTest : BaseTest() {
    @Inject
    private lateinit var parser: JavadocParser
    @Inject
    private lateinit var config: JavabotConfig
    @Inject
    private lateinit var javadocClassDao: JavadocClassDao

    @Test
    fun targeted() {
        val api = JavadocApi(config, "JDK", version = "11")

        val javadocDir = parser.extractJavadocContent(api)
        val type = JavadocType.discover(javadocDir)
        Assert.assertEquals(type, JAVA11)
//        parse(type, api, javadocDir, "java.base/java/util/Map.html")
    }

    @Test
    fun targetedServlet() {
        val api = JavadocApi(config, "Servlet", "javax.servlet", "javax.servlet-api", JavadocTest.servletVersion)

        val javadocDir = parser.extractJavadocContent(api)
        val type = JavadocType.discover(javadocDir)
        Assert.assertEquals(type, JAVA6)
//        parse(type, api, javadocDir, "javax/servlet/http/Cookie.html")
    }

    @Test
    fun targetedJavaEE() {
        val api = JavadocApi(config, "JavaEE7", "javax", "javaee-api", "7.0")

        val javadocDir = parser.extractJavadocContent(api)
        val type = JavadocType.discover(javadocDir)
        Assert.assertEquals(type, JAVA7)
//        parse(type, api, javadocDir, "javax/jws/soap/InitParam.html")
    }

    @Test
    fun targetedGuava() {
        val api = JavadocApi(config, "guava", "com.google.guava", "guava", "22.0")

        val javadocDir = parser.extractJavadocContent(api)
        val type = JavadocType.discover(javadocDir)
        Assert.assertEquals(type, JAVA8)
//        parse(type, api, javadocDir, "com/google/common/collect/ArrayTable.html")
    }

    private fun parse(type: JavadocType, api: JavadocApi, javadocDir: File, path: String) {
        type.create(api, File(javadocDir, path).absolutePath).parse(javadocClassDao)
    }
}
