package javabot.javadoc

import javabot.BaseTest
import javabot.JavabotConfig
import javabot.admin.JavadocTest
import javabot.dao.ApiDao
import javabot.dao.JavadocClassDao
import javabot.javadoc.JavadocType.JAVA11
import javabot.javadoc.JavadocType.JAVA6
import javabot.javadoc.JavadocType.JAVA7
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
    fun jdk() {
        val api = JavadocApi(config, "JDK", version="11")
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

        val api = JavadocApi(config, apiName, version = "11")
        datastore.save(api)

        val javadocDir = parser.extractJavadocContent(api)
        val type = JavadocType.discover(javadocDir)
        Assert.assertEquals(type, JAVA11)
        parse(type, api, javadocDir, "java.base/java/lang/Object.html")
        parse(type, api, javadocDir, "java.base/java/lang/String.html")
        parse(type, api, javadocDir, "java.base/java/util/Map.html")
    }

    private fun parse(type: JavadocType, api: JavadocApi, javadocDir: File, path: String) {
        type.create(api, File(javadocDir, path).absolutePath).parse(javadocClassDao)
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
        parse(type, api, javadocDir, "javax/servlet/http/Cookie.html")
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
        parse(type, api, javadocDir, "javax/enterprise/concurrent/ContextService.html")
        parse(type, api, javadocDir, "javax/ejb/AsyncResult.html")
        parse(type, api, javadocDir, "javax/persistence/Cache.html")
        parse(type, api, javadocDir, "javax/jws/soap/InitParam.html")
    }

    @Test
    fun targetedGuava() {
        val apiName = "guava"
        apiDao.delete(apiName)

        val api = JavadocApi(config, apiName, "com.google.guava", "guava", "19.0")
        datastore.save(api)

        val javadocDir = parser.extractJavadocContent(api)
        val type = JavadocType.discover(javadocDir)
        Assert.assertEquals(type, JAVA7)
        parse(type, api, javadocDir, "com/google/common/escape/Escapers.Builder.html")
    }
}
