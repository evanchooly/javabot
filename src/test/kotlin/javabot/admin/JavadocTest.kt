package javabot.admin

import com.google.inject.Injector
import com.jayway.awaitility.Duration
import javabot.BaseTest
import javabot.JavabotConfig
import javabot.dao.ApiDao
import javabot.dao.JavadocClassDao
import javabot.model.javadoc.JavadocApi
import javabot.model.ApiEvent
import javabot.operations.JavadocOperation
import org.slf4j.LoggerFactory
import org.testng.Assert
import org.testng.annotations.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Test
class JavadocTest : BaseTest() {
    companion object {
        private val LOG = LoggerFactory.getLogger(JavadocTest::class.java)
        const val servletVersion = "3.0.1"
    }

    @Inject
    private lateinit var injector: Injector

    @Inject
    private lateinit var apiDao: ApiDao

    @Inject
    private lateinit var classDao: JavadocClassDao

    @Inject
    private lateinit var config: JavabotConfig

    @Inject
    private lateinit var operation: JavadocOperation

    @Test(dependsOnMethods = ["coreJavadoc"])
    fun servlets() {
        val apiName = "Servlet"
        dropApi(apiName)
        addApi(apiName, "javax.servlet", "javax.servlet-api", servletVersion)
        checkServlets(apiName)
    }

    @Test(dependsOnMethods = ["servlets"])
    fun reloadServlets() {
        val apiName = "Servlet"
        val event = ApiEvent.reload(TEST_USER.nick, apiName)
        eventDao.save(event)
        waitForEvent(event, "reloading $apiName", Duration(30, TimeUnit.MINUTES))
        messages.clear()
        checkServlets(apiName)
    }

    private fun checkServlets(apiName: String) {
        val api = apiDao.find(apiName)
        Assert.assertNotNull(classDao.getClass(api, "javax.servlet.http", "HttpServletRequest"),
                "Should find an entry for ${apiName}/javax.servlet.http.HttpServletRequest")

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

    @Test(dependsOnMethods = ["coreJavadoc"])
    fun javaee() {
        val apiName = "JavaEE7"
        dropApi(apiName)
        addApi(apiName, "javax", "javaee-api", "7.0")
        verifyMapCount()
        scanForResponse(operation.handleMessage(message("~javadoc Annotated")), "javax/enterprise/inject/spi/Annotated.html")
        scanForResponse(operation.handleMessage(message("~javadoc Annotated.getAnnotation(*)")),
                "javax/enterprise/inject/spi/Annotated.html#getAnnotation")
        scanForResponse(operation.handleMessage(message("~javadoc ContextService")), "javax/enterprise/concurrent/ContextService.html")
        scanForResponse(operation.handleMessage(message("~javadoc ContextService.createContextualProxy(*)")),
                "createContextualProxy(java.lang.Object, java.lang.Class...)")
        scanForResponse(operation.handleMessage(message("~javadoc ContextService.createContextualProxy(*)")),
                "createContextualProxy(java.lang.Object, java.util.Map, java.lang.Class...)")
        scanForResponse(operation.handleMessage(message("~javadoc ContextService.createContextualProxy(*)")),
                "createContextualProxy(T, java.lang.Class)")
        scanForResponse(operation.handleMessage(message("~javadoc ContextService.createContextualProxy(*)")),
                "createContextualProxy(T, java.util.Map, java.lang.Class)")
        scanForResponse(operation.handleMessage(message("~javadoc PartitionPlan")), "javax/batch/api/partition/PartitionPlan.html")
        scanForResponse(operation.handleMessage(message("~javadoc PartitionPlan.setPartitionProperties(Properties[])")),
                "javax/batch/api/partition/PartitionPlan.html#setPartitionProperties(java.util.Properties[])")
    }

    @Test
    fun coreJavadoc() {
        bot
        if (java.lang.Boolean.valueOf(System.getProperty("dropJDK", "false"))) {
            LOG.debug("Dropping JDK API")
            dropApi("JDK")
            LOG.debug("Done")
        }
        var api = apiDao.find("JDK")
        if (api == null) {
            val apiEvent = ApiEvent.add(TEST_USER.nick, JavadocApi(config, "JDK", version = "11"))
            injector.injectMembers(apiEvent)
            apiEvent.handle()
            messages.clear()
            api = apiDao.find("JDK")
        }
        Assert.assertEquals(classDao.getClass(api, "Map").size, 1)
        Assert.assertNotNull(classDao.getClass(api, "java.lang", "Integer"), "Should find an entry for ${api?.name}'s java.lang.Integer")
        Assert.assertNotNull(classDao.getClass(api, "java.util", "List"), "Should find an entry for ${api?.name}'s java.util.List")
        Assert.assertNotNull(classDao.getClass(api, "java.sql", "ResultSet"), "Should find an entry for ${api?.name}'s java.sql.ResultSet")
        Assert.assertNotNull(classDao.getClass(api, "java.util.Map.Entry"), "Should find an entry for ${api?.name}'s java.util.Map.Entry")
        Assert.assertNotNull(classDao.getClass(api, "Map.Entry"), "Should find an entry for ${api?.name}'s java.util.Map.Entry")
        scanForResponse(operation.handleMessage(message("~javadoc Map.Entry")),
                "${config.url()}/javadoc/JDK/11/index.html?java/util/Map.Entry.html")
        scanForResponse(operation.handleMessage(message("~javadoc String.chars()")),
                "${config.url()}/javadoc/JDK/11/index.html?java/lang/CharSequence.html#chars()")
        scanForResponse(operation.handleMessage(message("~javadoc ResultSet.getInt(*)")),
                "${config.url()}/javadoc/JDK/11/index.html?java/sql/ResultSet.html#getInt")
    }

    private fun verifyMapCount() {
        val list = classDao.getClass(null, "Map")
        Assert.assertEquals(list.size, 1, "Should have found only 1:  $list")
    }

    @Test
    fun guava() {
        val apiName = "guava"
        dropApi(apiName)
        addApi(apiName, "com.google.guava", "guava", "19.0")
        val api = apiDao.find("guava")
        Assert.assertEquals(classDao.getClass(api, "ArrayTable").size, 1)
        Assert.assertEquals(classDao.getClass(api, "AbstractCache").size, 1)
        Assert.assertEquals(classDao.getClass(api, "ArrayBasedCharEscaper").size, 1)
    }

    @Test(dependsOnMethods = ["guava"])
    fun reload() {
        val api = apiDao.find("guava")
        classDao.delete(classDao.getClass(api, "AbstractCache")[0])
        classDao.delete(classDao.getClass(api, "ArrayTable")[0])
        Assert.assertEquals(classDao.getClass(api, "AbstractCache").size, 0)
        Assert.assertEquals(classDao.getClass(api, "ArrayTable").size, 0)
        reloadApi("guava")
        Assert.assertEquals(classDao.getClass(api, "AbstractCache").size, 1)
        Assert.assertEquals(classDao.getClass(api, "ArrayTable").size, 1)
    }


    private fun addApi(apiName: String, groupId: String, artifactId: String, version: String) {
        val apiEvent = ApiEvent.add(TEST_USER.nick, JavadocApi(config, apiName, groupId, artifactId, version))
        injector.injectMembers(apiEvent)
        apiEvent.handle()
        LOG.debug("done waiting for event to finish")
        messages.clear()
    }

    private fun dropApi(apiName: String) {
        val apiEvent = ApiEvent.drop(TEST_USER.nick, apiName)
        injector.injectMembers(apiEvent)
        apiEvent.handle()
        messages.clear()
    }

    private fun reloadApi(apiName: String) {
        val apiEvent = ApiEvent.reload(TEST_USER.nick, apiName)
        injector.injectMembers(apiEvent)
        apiEvent.handle()
        messages.clear()
    }
}
