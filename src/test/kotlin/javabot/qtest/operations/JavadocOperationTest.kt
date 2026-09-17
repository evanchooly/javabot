package javabot.qtest.operations

import com.antwerkz.sofia.Sofia
import io.quarkus.test.junit.QuarkusTest
import javabot.BaseTest
import javabot.dao.JavadocClassDao
import javabot.operations.JavadocOperation
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@QuarkusTest
class JavadocOperationTest : BaseTest() {
    companion object {
        val STRING_URL = "java.base/java/lang/String.html#"
    }

    private val operation: JavadocOperation by lazy {
        injector.getInstance(JavadocOperation::class.java)
    }
    private val javadocClassDao: JavadocClassDao by lazy {
        injector.getInstance(JavadocClassDao::class.java)
    }

    @BeforeEach
    fun jdk() {
        loadApi("JDK", version = "11")
    }

    @Test
    fun constructors() {
        scanForResponse(
            operation.handleMessage(message("~javadoc String(char[])")),
            "$STRING_URL<init>(char[])",
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc java.lang.String(char[])")),
            "$STRING_URL<init>(char[])",
        )
    }

    @Test
    fun methods() {
        scanForResponse(
            operation.handleMessage(message("~javadoc String.split(String)")),
            "${STRING_URL}split(java.lang.String)",
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc -jdk String.split(String)")),
            "${STRING_URL}split(java.lang.String)",
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc String.split(java.lang.String)")),
            "${STRING_URL}split(java.lang.String)",
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc String.join(*)")),
            "${STRING_URL}join(",
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc String.split(*)")),
            "${STRING_URL}split(java.lang.String)",
        )
    }

    @Test
    fun nestedClasses() {
        scanForResponse(
            operation.handleMessage(message("~javadoc Map.Entry")),
            "java/util/Map.Entry.html",
        )
    }

    @Test
    fun format() {
        scanForResponse(
            operation.handleMessage(message("~javadoc String.format(*)")),
            "java/lang/String.html#format(java.util.Locale,java.lang.String,java.lang.Object...)",
        )
    }

    @Test
    fun doFinal() {
        scanForResponse(
            operation.handleMessage(message("~javadoc String.valueOf(*)")),
            Sofia.tooManyResults(TEST_USER.nick),
        )
    }

    @Test
    fun fields() {
        scanForResponse(
            operation.handleMessage(message("~javadoc System.in")),
            "java/lang/System.html#in",
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc Integer.MAX_VALUE")),
            "java/lang/Integer.html#MAX_VALUE",
        )
        scanForResponse(
            operation.handleMessage(message("~javadoc -jdk System.in")),
            "java/lang/System.html#in",
        )
    }

    @Test
    fun inherited() {
        scanForResponse(
            operation.handleMessage(message("~javadoc ArrayList.listIterator(*)")),
            "java/util/ArrayList.html#listIterator(int)",
        )
    }

    @Test
    fun packagePrivate() {
        scanForResponse(
            operation.handleMessage(message("~javadoc ASCII)")),
            "I have no documentation for ASCII",
        )
    }
}
