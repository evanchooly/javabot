package javabot.qtest.operations

import com.antwerkz.sofia.Sofia
import io.quarkus.test.junit.QuarkusTest
import javabot.BaseTest
import javabot.operations.BrowseOperation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@QuarkusTest
class BrowseOperationTest : BaseTest() {
    private val browseOperation: BrowseOperation by lazy {
        injector.getInstance(BrowseOperation::class.java)
    }

    @Test
    @Tag("operations")
    fun testNonMatchingInput() {
        val response = browseOperation.handleMessage(message("~test pong is pong"))
        assertEquals(0, response.size)
    }

    @Test
    @Tag("operations")
    fun testClasses() {
        val response = browseOperation.handleMessage(message("~browse Files"))
        assertEquals(1, response.size)
        assertTrue(response[0].value.startsWith("References matching 'Files' can be found at: "))
    }

    @Test
    @Tag("operations")
    fun testMethodReference() {
        val response = browseOperation.handleMessage(message("~browse String.indexOf(String)"))
        assertEquals(1, response.size)
        assertTrue(
            response[0]
                .value
                .startsWith("A reference matching 'String.indexOf(String)' can be found at:")
        )
    }

    @Test
    @Tag("operations")
    fun testModules() {
        val response = browseOperation.handleMessage(message("~browse guava Files"))
        assertEquals(1, response.size)
        assertTrue(response[0].value.startsWith("A reference matching 'Files' can be found at: "))
    }

    // @Test // needs to test entire bot message handler, not the operation directly
    private fun testNoTrigger() {
        val response = browseOperation.handleMessage(message("browse guava Files"))
        assertEquals(0, response.size)
    }

    @Test
    @Tag("operations")
    fun testNoValidResponseClass() {
        val response = browseOperation.handleMessage(message("~browse FilesMcGee"))
        assertEquals(1, response.size)
        assertEquals("No source matching `FilesMcGee` found.", response[0].value)
    }

    @Test
    @Tag("operations")
    fun testNoValidResponseModule() {
        val response = browseOperation.handleMessage(message("~browse guavaMcGee Files"))
        assertEquals(1, response.size)
        assertEquals("No source matching `Files` and module `guavaMcGee` found.", response[0].value)
    }

    @Test
    @Tag("operations")
    fun testNoValidResponseModuleWithClass() {
        val response = browseOperation.handleMessage(message("~browse guava filesmcgee"))
        assertEquals(1, response.size)
        assertEquals("No source matching `filesmcgee` and module `guava` found.", response[0].value)
    }

    @Test
    @Tag("operations")
    fun testNoValidResponseModuleAndClass() {
        val response = browseOperation.handleMessage(message("~browse guavaMcGee FilesMcGee"))
        assertEquals(1, response.size)
        assertEquals(
            "No source matching `FilesMcGee` and module `guavaMcGee` found.",
            response[0].value,
        )
    }

    @Test
    @Tag("operations")
    fun testBrowseHelp() {
        val response = browseOperation.handleMessage(message("~browse -help"))
        assertEquals(1, response.size)
        assertEquals(Sofia.browseHelp(), response[0].value)
    }
}
