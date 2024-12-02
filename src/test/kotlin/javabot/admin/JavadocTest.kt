package javabot.admin

import jakarta.inject.Inject
import java.io.File
import javabot.BaseTest
import javabot.JavabotConfig
import javabot.dao.JavadocClassDao
import javabot.model.ApiEvent
import javabot.model.javadoc.JavadocApi
import javabot.operations.JavadocOperation
import org.testng.Assert
import org.testng.Assert.assertNotNull
import org.testng.Assert.fail
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

@Test
class JavadocTest() : BaseTest() {
    companion object {
        val eeVersion = "8.0.0"
        val eeApiName = "JakartaEE8"
    }

    @Inject private lateinit var classDao: JavadocClassDao

    @Inject private lateinit var config: JavabotConfig

    @Inject private lateinit var operation: JavadocOperation

    @BeforeClass
    fun drops() {
        apiDao.delete(eeApiName)
    }

    private fun checkServlets(api: JavadocApi) {
        assertNotNull(
            classDao.getClass(api, "jakarta.servlet.http", "HttpServletRequest"),
            "Should find an entry for ${api.name}/jakarta.servlet.http.HttpServletRequest"
        )

        scanForResponse(
            operation.handleMessage(message("~javadoc HttpServlet")),
            "javax/servlet/http/HttpServlet.html"
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc HttpServlet.doGet(*)")),
            "javax/servlet/http/HttpServlet.html#doGet"
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc HttpServletRequest")),
            "javax/servlet/http/HttpServletRequest.html"
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc HttpServletRequest.getMethod()")),
            "javax/servlet/http/HttpServletRequest.html#getMethod"
        )
        checkServletFile()
    }

    private fun checkServletFile() {
        val uri = File("javadoc/$eeApiName/$eeVersion/javax/servlet/http/HttpServlet.html")
        Assert.assertTrue(uri.exists())
    }

    @Test(dependsOnMethods = ["core"])
    fun jakartaEE() {
        val api = loadApi(eeApiName, "jakarta.platform", "jakarta.jakartaee-api", "8.0.0")
        verifyMapCount()
        scanForResponse(
            operation.handleMessage(message("~javadoc Annotated")),
            "javax/enterprise/inject/spi/Annotated.html"
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc Annotated.getAnnotation(*)")),
            "javax/enterprise/inject/spi/Annotated.html#getAnnotation"
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc ContextService")),
            "javax/enterprise/concurrent/ContextService.html"
        )

        scanForResponse(
            operation.handleMessage(message("~javadoc ContextService.createContextualProxy(*)")),
            "createContextualProxy-java.lang.Object-java.lang.Class...-"
        )

        scanForResponse(
            operation.handleMessage(message("~javadoc ContextService.createContextualProxy(*)")),
            "createContextualProxy-java.lang.Object-java.util.Map-java.lang.Class...-"
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc ContextService.createContextualProxy(*)")),
            "createContextualProxy-T-java.lang.Class-"
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc ContextService.createContextualProxy(*)")),
            "createContextualProxy-T-java.util.Map-java.lang.Class-"
        )

        checkServlets(api)
    }

    @Test
    fun core() {
        val api = loadApi("JDK", version = "11")
        Assert.assertEquals(classDao.getClass(api, "Map").size, 1)
        assertNotNull(
            classDao.getClass(api, "java.lang", "Integer"),
            "Should find an entry for ${api.name}'s java.lang.Integer"
        )
        assertNotNull(
            classDao.getClass(api, "java.util", "List"),
            "Should find an entry for ${api.name}'s java.util.List"
        )
        assertNotNull(
            classDao.getClass(api, "java.sql", "ResultSet"),
            "Should find an entry for ${api.name}'s java.sql.ResultSet"
        )
        assertNotNull(
            classDao.getClass(api, "java.util.Map.Entry"),
            "Should find an entry for ${api.name}'s java.util.Map.Entry"
        )
        assertNotNull(
            classDao.getClass(api, "Map.Entry"),
            "Should find an entry for ${api.name}'s java.util.Map.Entry"
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc Map.Entry")),
            "${api.baseUrl}/java.base/java/util/Map.Entry.html"
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc String.chars()")),
            "${api.baseUrl}/java.base/java/lang/String.html#chars()"
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc ResultSet.getInt(*)")),
            "${api.baseUrl}/java.sql/java/sql/ResultSet.html#getInt"
        )
    }

    private fun verifyMapCount() {
        val list = classDao.getClass(null, "Map")
        Assert.assertEquals(list.size, 1, "Should have found only 1:  $list")
    }

    @Test(dependsOnMethods = ["core", "jakartaEE"])
    fun guava() {
        val apiName = "guava"
        val guava = loadApi(apiName, "com.google.guava", "guava", "28.2-jre")
        val classCount = classDao.count()

        Assert.assertEquals(classDao.getClass(guava, "ArrayTable").size, 1)
        Assert.assertEquals(classDao.getClass(guava, "AbstractCache").size, 1)
        Assert.assertEquals(classDao.getClass(guava, "ArrayBasedCharEscaper").size, 1)
        Assert.assertEquals(classDao.getClass(null, "ArrayList").size, 1)
        Assert.assertEquals(classDao.getClass(null, "HttpServlet").size, 1)

        classDao.delete(classDao.getClass(guava, "AbstractCache")[0])
        classDao.delete(classDao.getClass(guava, "ArrayTable")[0])

        Assert.assertEquals(classDao.count(), classCount - 2)

        Assert.assertEquals(classDao.getClass(null, "ArrayList").size, 1)
        Assert.assertEquals(classDao.getClass(null, "HttpServlet").size, 1)

        Assert.assertEquals(classDao.getClass(guava, "AbstractCache").size, 0)
        Assert.assertEquals(classDao.getClass(guava, "ArrayTable").size, 0)

        val event = ApiEvent.reload(TEST_USER.nick, apiName)
        //        injector.injectMembers(event)
        fail()
        event.handle()

        Assert.assertEquals(classDao.getClass(event.api, "AbstractCache").size, 1)
        Assert.assertEquals(classDao.getClass(event.api, "ArrayTable").size, 1)
        Assert.assertEquals(classDao.getClass(null, "ArrayList").size, 1)
        Assert.assertEquals(classDao.getClass(null, "HttpServlet").size, 1)
    }
}
