package javabot.admin

import com.jayway.awaitility.Awaitility
import com.jayway.awaitility.Duration
import javabot.BaseTest
import javabot.JavabotConfig
import javabot.dao.ApiDao
import javabot.dao.JavadocClassDao
import javabot.javadoc.JavadocApi
import javabot.model.ApiEvent
import javabot.model.EventType
import javabot.operations.JavadocOperation
import org.slf4j.LoggerFactory
import org.testng.Assert
import org.testng.annotations.Test
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Test
class JavadocTest : BaseTest() {

    @Inject
    private lateinit var apiDao: ApiDao

    @Inject
    private lateinit var javadocClassDao: JavadocClassDao

    @Inject
    private lateinit var config: JavabotConfig

    @Inject
    private lateinit var operation: JavadocOperation

    @Test
    fun servlets() {
        val apiName = "Servlet"
        dropApi(apiName)
        addApi(apiName, "javax.servlet", "javax.servlet-api", "3.0.1")
        checkServlets(apiName)
    }

    @Test(dependsOnMethods = arrayOf("servlets"))
    fun reloadServlets() {
        val apiName = "Servlet"
        val event = ApiEvent(TEST_USER.nick, EventType.RELOAD, apiDao.find(apiName)?.id)
        eventDao.save(event)
        waitForEvent(event, "reloading " + apiName, Duration(30, TimeUnit.MINUTES))
        messages.clear()
        checkServlets(apiName)
    }

    @Test
    fun deleteJavadoc() {
        val apiName = "Servlet-html"
        dropApi(apiName)
        addApi(apiName, "javax.servlet", "javax.servlet-api", "3.0.1")

        val host = config.databaseHost()
        val port = config.databasePort()
        val database = config.databaseName()
        val uri = URI("gridfs://$host:$port/$database.javadoc/Servlet-html/javax/servlet/http/HttpServlet.html")
        Assert.assertTrue(Files.exists(Paths.get(uri)))

        dropApi(apiName)
        Assert.assertFalse(Files.exists(Paths.get(uri)))
    }

    private fun checkServlets(apiName: String) {
        val api = apiDao.find(apiName)
        Assert.assertNotNull(javadocClassDao.getClass(api, "javax.servlet.http", "HttpServletRequest"),
                "Should find an entry for ${apiName}/javax.servlet.http.HttpServletRequest")

        scanForResponse(operation.handleMessage(message("~javadoc HttpServlet")), "javax/servlet/http/HttpServlet.html")
        scanForResponse(operation.handleMessage(message("~javadoc HttpServlet.doGet(*)")), "javax/servlet/http/HttpServlet.html#doGet")
        scanForResponse(operation.handleMessage(message("~javadoc HttpServletRequest")), "javax/servlet/http/HttpServletRequest.html")
        scanForResponse(operation.handleMessage(message("~javadoc HttpServletRequest.getMethod()")),
                "javax/servlet/http/HttpServletRequest.html#getMethod")
        checkServletFile(true)
    }

    private fun checkServletFile(result: Boolean) {
        val host = config.databaseHost()
        val port = config.databasePort()
        val database = config.databaseName()
        val uri = URI("gridfs://$host:$port/$database.javadoc/Servlet/javax/servlet/http/HttpServlet.html")
        Assert.assertEquals(Files.exists(Paths.get(uri)), result)
    }

    @Test(enabled = false)
    fun javaee() {
        val apiName = "JavaEE7"
        dropApi(apiName)
        addApi(apiName, "javax", "javaee-api", "7.0")
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
    fun jdk() {
        bot
        if (java.lang.Boolean.valueOf(System.getProperty("dropJDK", "false"))) {
            LOG.debug("Dropping JDK API")
            dropApi("JDK")
            LOG.debug("Done")
        }
        var api: JavadocApi? = apiDao.find("JDK")
        if (api == null) {
            val event = ApiEvent(TEST_USER.nick, "JDK", "", "", "")
            eventDao.save(event)
            waitForEvent(event, "adding JDK", Duration(30, TimeUnit.MINUTES))
            messages.clear()
            api = apiDao.find("JDK")
        }
        Assert.assertNotNull(javadocClassDao.getClass(api, "java.lang", "Integer"),
                "Should find an entry for ${api?.name}'s java.lang.Integer")
        scanForResponse(operation.handleMessage(message("~javadoc String.chars()")),
                "${config.url()}/javadoc/JDK/1.8/index.html?java/lang/CharSequence.html#chars()")
    }

    private fun addApi(apiName: String, groupId: String, artifactId: String, version: String) {
        val event = ApiEvent(TEST_USER.nick, apiName, groupId, artifactId, version)
        eventDao.save(event)
        waitForEvent(event, "adding ${apiName}", Duration(5, TimeUnit.MINUTES))
        LOG.debug("done waiting for event to finish")
        messages.clear()
    }

    private fun dropApi(apiName: String) {
        eventDao.save(ApiEvent(TEST_USER.nick, EventType.DELETE, apiName))
        Awaitility.await()
                .atMost(60, TimeUnit.SECONDS)
                .until<Boolean> { apiDao.find(apiName) == null }
        messages.clear()
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(JavadocTest::class.java)
    }
}
