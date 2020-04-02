package javabot.admin

import javabot.BaseTest
import javabot.JavabotConfig
import javabot.dao.JavadocClassDao
import javabot.model.ApiEvent
import javabot.model.javadoc.JavadocApi
import javabot.model.javadoc.JavadocClass
import javabot.model.javadoc.JavadocField
import javabot.model.javadoc.JavadocMethod
import javabot.operations.JavadocOperation
import org.bson.Document
import org.testng.Assert
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.inject.Inject

@Test
class JavadocTest : BaseTest() {
    companion object {
        const val servletVersion = "3.0.1"
    }

    @Inject
    private lateinit var classDao: JavadocClassDao

    @Inject
    private lateinit var config: JavabotConfig

    @Inject
    private lateinit var operation: JavadocOperation

    @BeforeClass
    fun drops() {
        for(type in listOf(JavadocApi::class.java, JavadocClass::class.java, JavadocMethod::class.java, JavadocField::class.java)) {
            val collection = datastore.mapper.getCollection(type)
            println("Deleting all in ${collection.namespace.collectionName}")
            collection.deleteMany(Document())
        }
        apiDao.delete("Servlet")
        apiDao.delete("JakartaEE8")
    }

    private fun checkServlets(api: JavadocApi) {
        Assert.assertNotNull(classDao.getClass(api, "javax.servlet.http", "HttpServletRequest"),
                "Should find an entry for ${api.name}/javax.servlet.http.HttpServletRequest")

        scanForResponse(operation.handleMessage(message("~javadoc HttpServlet")), "javax/servlet/http/HttpServlet.html")
        scanForResponse(operation.handleMessage(message("~javadoc HttpServlet.doGet(*)")), "javax/servlet/http/HttpServlet.html#doGet")
        scanForResponse(operation.handleMessage(message("~javadoc HttpServletRequest")), "javax/servlet/http/HttpServletRequest.html")
        scanForResponse(operation.handleMessage(message("~javadoc HttpServletRequest.getMethod()")),
                "javax/servlet/http/HttpServletRequest.html#getMethod")
        checkServletFile(true)
    }

    private fun checkServletFile(result: Boolean) {
        val uri = File("javadoc/Servlet/${servletVersion}/javax/servlet/http/HttpServlet.html").toURI()
        Assert.assertEquals(Files.exists(Paths.get(uri)), result)
    }

    @Test(dependsOnMethods = ["core"])
    fun jakartaEE() {
        val apiName = "JakartaEE8"
        val api = loadApi(apiName, "jakarta.platform", "jakarta.jakartaee-api", "8.0.0")
        verifyMapCount()
        scanForResponse(operation.handleMessage(message("~javadoc Annotated")), "javax/enterprise/inject/spi/Annotated.html")
        scanForResponse(operation.handleMessage(message("~javadoc Annotated.getAnnotation(*)")),
                "javax/enterprise/inject/spi/Annotated.html#getAnnotation")
        scanForResponse(operation.handleMessage(message("~javadoc ContextService")), "javax/enterprise/concurrent/ContextService.html")

        scanForResponse(operation.handleMessage(message("~javadoc ContextService.createContextualProxy(*)")),
                "createContextualProxy-java.lang.Object-java.lang.Class...-")

        scanForResponse(operation.handleMessage(message("~javadoc ContextService.createContextualProxy(*)")),
                "createContextualProxy-java.lang.Object-java.util.Map-java.lang.Class...-")
        scanForResponse(operation.handleMessage(message("~javadoc ContextService.createContextualProxy(*)")),
                "createContextualProxy-T-java.lang.Class-")
        scanForResponse(operation.handleMessage(message("~javadoc ContextService.createContextualProxy(*)")),
                "createContextualProxy-T-java.util.Map-java.lang.Class-")

        checkServlets(api)
    }

    @Test
    fun core() {
        bot
        val api = loadApi("JDK", version = "11")
        Assert.assertEquals(classDao.getClass(api, "Map").size, 1)
        Assert.assertNotNull(classDao.getClass(api, "java.lang", "Integer"), "Should find an entry for ${api.name}'s java.lang.Integer")
        Assert.assertNotNull(classDao.getClass(api, "java.util", "List"), "Should find an entry for ${api.name}'s java.util.List")
        Assert.assertNotNull(classDao.getClass(api, "java.sql", "ResultSet"), "Should find an entry for ${api.name}'s java.sql.ResultSet")
        Assert.assertNotNull(classDao.getClass(api, "java.util.Map.Entry"), "Should find an entry for ${api.name}'s java.util.Map.Entry")
        Assert.assertNotNull(classDao.getClass(api, "Map.Entry"), "Should find an entry for ${api.name}'s java.util.Map.Entry")
        scanForResponse(operation.handleMessage(message("~javadoc Map.Entry")),
                "${config.url()}/javadoc/JDK/11/java.base/java/util/Map.Entry.html")
        scanForResponse(operation.handleMessage(message("~javadoc String.chars()")),
                "${config.url()}/javadoc/JDK/11/java.base/java/lang/String.html#chars()")
        scanForResponse(operation.handleMessage(message("~javadoc ResultSet.getInt(*)")),
                "${config.url()}/javadoc/JDK/11/java.sql/java/sql/ResultSet.html#getInt")
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
        injector.injectMembers(event)
        event.handle()

        Assert.assertEquals(classDao.count(), classCount)

        Assert.assertEquals(classDao.getClass(event.api, "AbstractCache").size, 1)
        Assert.assertEquals(classDao.getClass(event.api, "ArrayTable").size, 1)
        Assert.assertEquals(classDao.getClass(null, "ArrayList").size, 1)
        Assert.assertEquals(classDao.getClass(null, "HttpServlet").size, 1)
    }
}
