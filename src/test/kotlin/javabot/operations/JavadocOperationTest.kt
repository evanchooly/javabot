package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.BaseTest
import javabot.dao.JavadocClassDao
import javax.inject.Inject
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test

@Test
class JavadocOperationTest : BaseTest() {
    companion object {
        val STRING_URL = "java.base/java/lang/String.html#"
    }

    @Inject private lateinit var operation: JavadocOperation
    @Inject protected lateinit var javadocClassDao: JavadocClassDao

    @BeforeTest
    fun jdk() {
        loadApi("JDK", version = "11")
    }

    fun constructors() {
        scanForResponse(
            operation.handleMessage(message("~javadoc String(char[])")),
            "$STRING_URL<init>(char[])"
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc java.lang.String(char[])")),
            "$STRING_URL<init>(char[])"
        )
    }

    fun methods() {
        scanForResponse(
            operation.handleMessage(message("~javadoc String.split(String)")),
            "${STRING_URL}split(java.lang.String)"
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc -jdk String.split(String)")),
            "${STRING_URL}split(java.lang.String)"
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc String.split(java.lang.String)")),
            "${STRING_URL}split(java.lang.String)"
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc String.join(*)")),
            "${STRING_URL}join("
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc String.split(*)")),
            "${STRING_URL}split(java.lang.String)"
        )
    }

    fun nestedClasses() {
        scanForResponse(
            operation.handleMessage(message("~javadoc Map.Entry")),
            "java/util/Map.Entry.html"
        )
    }

    fun format() {
        scanForResponse(
            operation.handleMessage(message("~javadoc String.format(*)")),
            "java/lang/String.html#format(java.util.Locale,java.lang.String,java.lang.Object...)"
        )
    }

    fun doFinal() {
        scanForResponse(
            operation.handleMessage(message("~javadoc String.valueOf(*)")),
            Sofia.tooManyResults(TEST_USER.nick)
        )
    }

    fun fields() {
        scanForResponse(
            operation.handleMessage(message("~javadoc System.in")),
            "java/lang/System.html#in"
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc Integer.MAX_VALUE")),
            "java/lang/Integer.html#MAX_VALUE"
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc -jdk System.in")),
            "java/lang/System.html#in"
        )
    }

    fun inherited() {
        scanForResponse(
            operation.handleMessage(message("~javadoc ArrayList.listIterator(*)")),
            "java/util/ArrayList.html#listIterator(int)"
        )
    }

    fun packagePrivate() {
        scanForResponse(
            operation.handleMessage(message("~javadoc ASCII)")),
            "I have no documentation for ASCII"
        )
    }
}
