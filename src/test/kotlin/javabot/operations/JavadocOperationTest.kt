package javabot.operations

import com.antwerkz.sofia.Sofia
import com.jayway.awaitility.Duration
import javabot.BaseMessagingTest
import javabot.dao.ApiDao
import javabot.model.ApiEvent
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test
import java.net.MalformedURLException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Test//(dependsOnMethods = {"jdk"})
public class JavadocOperationTest : BaseMessagingTest() {
    @Inject
    protected lateinit var apiDao: ApiDao

    @BeforeTest
    public fun getBot() {
        javabot
    }

    @Throws(MalformedURLException::class)
    public fun jdk() {
        val api = apiDao.find("JDK")
        if (api == null) {
            val event = ApiEvent(testUser.nick, "JDK", "http://docs.oracle.com/javase/8/docs/api", "")
            eventDao.save(event)
            waitForEvent(event, "adding JDK", Duration(30, TimeUnit.MINUTES))
        }
        messages.get()

    }

    @Throws(MalformedURLException::class)
    public fun constructors() {
        jdk()
        scanForResponse("~javadoc java.lang.String(char[])", "java/lang/String.html")
        scanForResponse("~javadoc String(char[])", "java/lang/String.html#String-char[]-")
    }

    @Throws(MalformedURLException::class)
    public fun methods() {
        jdk()
        scanForResponse("~javadoc String.split(String)", "java/lang/String.html#split-java.lang.String-")
        scanForResponse("~javadoc -jdk String.split(String)", "java/lang/String.html#split-java.lang.String-")
        scanForResponse("~javadoc String.split(java.lang.String)", "java/lang/String.html#split-java.lang.String-")
        scanForResponse("~javadoc String.join(*)", "java/lang/String.html#join-")
        scanForResponse("${javabot.get().getNick()} javadoc String.split(*)", "java/lang/String.html#split-java.lang.String-")
    }

    @Throws(MalformedURLException::class)
    public fun nestedClasses() {
        jdk()
        scanForResponse("~javadoc Map.Entry", "java/util/Map.Entry.html")
    }

    @Throws(MalformedURLException::class)
    public fun format() {
        jdk()
        scanForResponse("~javadoc String.format(*)", "java/lang/String.html#format-java.util.Locale-java.lang.String-java.lang.Object[]")
    }

    @Test
    @Throws(MalformedURLException::class)
    public fun doFinal() {
        jdk()
        scanForResponse("~javadoc String.valueOf(*)", Sofia.tooManyResults(testUser.nick))
    }

    @Throws(MalformedURLException::class)
    public fun fields() {
        jdk()
        scanForResponse("~javadoc System.in", "java/lang/System.html#in")
        scanForResponse("~javadoc Integer.MAX_VALUE", "java/lang/Integer.html#MAX_VALUE")
        scanForResponse("~javadoc -jdk System.in", "java/lang/System.html#in")
    }

    @Throws(MalformedURLException::class)
    public fun inherited() {
        jdk()
        scanForResponse("~javadoc ArrayList.listIterator(*)", "java/util/ArrayList.html#listIterator-int-")
    }
}