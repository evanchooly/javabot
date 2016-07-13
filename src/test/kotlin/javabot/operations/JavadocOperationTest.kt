package javabot.operations

import com.antwerkz.sofia.Sofia
import com.jayway.awaitility.Duration
import javabot.BaseTest
import javabot.dao.ApiDao
import javabot.model.ApiEvent
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test
import java.net.MalformedURLException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Test//(dependsOnMethods = {"jdk"})
class JavadocOperationTest : BaseTest() {
    @Inject
    protected lateinit var apiDao: ApiDao

    @Inject
    private lateinit var operation: JavadocOperation

    @BeforeTest fun getBot() {
        bot
    }

    fun jdk() {
        val api = apiDao.find("JDK")
        if (api == null) {
            val event = ApiEvent(testUser.nick, "JDK", "http://docs.oracle.com/javase/8/docs/api", "")
            eventDao.save(event)
            waitForEvent(event, "adding JDK", Duration(30, TimeUnit.MINUTES))
        }
        messages.clear()
    }

    fun constructors() {
        jdk()
        scanForResponse(operation.handleMessage(message("~javadoc java.lang.String(char[])")), "java/lang/String.html")
        scanForResponse(operation.handleMessage(message("~javadoc String(char[])")), "java/lang/String.html#String-char[]-")
    }

    fun methods() {
        jdk()
        scanForResponse(operation.handleMessage(message("~javadoc String.split(String)")), "java/lang/String.html#split-java.lang.String-")
        scanForResponse(operation.handleMessage(message("~javadoc -jdk String.split(String)")),
                "java/lang/String.html#split-java.lang.String-")
        scanForResponse(operation.handleMessage(message("~javadoc String.split(java.lang.String)")),
                "java/lang/String.html#split-java.lang.String-")
        scanForResponse(operation.handleMessage(message("~javadoc String.join(*)")), "java/lang/String.html#join-")
        scanForResponse(operation.handleMessage(message("~javadoc String.split(*)")), "java/lang/String.html#split-java.lang.String-")
    }

    fun nestedClasses() {
        jdk()
        scanForResponse(operation.handleMessage(message("~javadoc Map.Entry")), "java/util/Map.Entry.html")
    }

    fun  format() {
        jdk()
        scanForResponse(operation.handleMessage(message("~javadoc String.format(*)")),
                "java/lang/String.html#format-java.util.Locale-java.lang.String-java.lang.Object[]")
    }

    @Test
    fun doFinal() {
        jdk()
        scanForResponse(operation.handleMessage(message("~javadoc String.valueOf(*)")), Sofia.tooManyResults(testUser.nick))
    }

    fun fields() {
        jdk()
        scanForResponse(operation.handleMessage(message("~javadoc System.in")), "java/lang/System.html#in")
        scanForResponse(operation.handleMessage(message("~javadoc Integer.MAX_VALUE")), "java/lang/Integer.html#MAX_VALUE")
        scanForResponse(operation.handleMessage(message("~javadoc -jdk System.in")), "java/lang/System.html#in")
    }

    fun inherited() {
        jdk()
        scanForResponse(operation.handleMessage(message("~javadoc ArrayList.listIterator(*)")),
                "java/util/ArrayList.html#listIterator-int-")
    }
}