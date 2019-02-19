package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.BaseTest
import javabot.JavabotConfig
import javabot.dao.ApiDao
import javabot.model.ApiEvent
import javabot.model.javadoc.JavadocApi
import org.slf4j.LoggerFactory
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test
import javax.inject.Inject

@Test
class JavadocOperationTest : BaseTest() {
    @Inject
    private lateinit var operation: JavadocOperation

    @BeforeTest
    fun jdk() {
        loadApi("JDK", version = "11")
    }

    fun constructors() {
        scanForResponse(operation.handleMessage(message("~javadoc String(char[])")), "java/lang/String.html#%3Cinit%3E(char%5B%5D)")
        scanForResponse(operation.handleMessage(message("~javadoc java.lang.String(char[])")), "java/lang/String.html")
    }

    fun methods() {
        scanForResponse(operation.handleMessage(message("~javadoc String.split(String)")), "java/lang/String.html#split(java.lang.String)")
        scanForResponse(operation.handleMessage(message("~javadoc -jdk String.split(String)")),
                "java/lang/String.html#split(java.lang.String)")
        scanForResponse(operation.handleMessage(message("~javadoc String.split(java.lang.String)")),
                "java/lang/String.html#split(java.lang.String)")
        scanForResponse(operation.handleMessage(message("~javadoc String.join(*)")), "java/lang/String.html#join(")
        scanForResponse(operation.handleMessage(message("~javadoc String.split(*)")), "java/lang/String.html#split(java.lang.String)")
    }

    fun nestedClasses() {
        scanForResponse(operation.handleMessage(message("~javadoc Map.Entry")), "java/util/Map.Entry.html")
    }

    fun  format() {
        scanForResponse(operation.handleMessage(message("~javadoc String.format(*)")),
                "java/lang/String.html#format(java.util.Locale,java.lang.String,java.lang.Object...)")
    }

    fun doFinal() {
        scanForResponse(operation.handleMessage(message("~javadoc String.valueOf(*)")), Sofia.tooManyResults(TEST_USER.nick))
    }

    fun fields() {
        scanForResponse(operation.handleMessage(message("~javadoc System.in")), "java/lang/System.html#in")
        scanForResponse(operation.handleMessage(message("~javadoc Integer.MAX_VALUE")), "java/lang/Integer.html#MAX_VALUE")
        scanForResponse(operation.handleMessage(message("~javadoc -jdk System.in")), "java/lang/System.html#in")
    }

    fun inherited() {
        scanForResponse(operation.handleMessage(message("~javadoc ArrayList.listIterator(*)")),
                "java/util/ArrayList.html#listIterator(int)")
    }

    fun packagePrivate() {
        scanForResponse(operation.handleMessage(message("~javadoc ASCII)")), "I have no documentation for ASCII")
    }
}
