package javabot.operations

import javabot.BaseTest
import org.testng.annotations.Test
import javax.inject.Inject
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Test(groups = arrayOf("operations"))
class BrowseOperationTest @Inject constructor(val browseOperation: BrowseOperation) : BaseTest() {
    @Test
    fun testNonMatchingInput() {
        val response = browseOperation.handleMessage(message("~test pong is pong"))
        assertEquals(0, response.size)
    }

    @Test
    fun testClasses() {
        val response = browseOperation.handleMessage(message("~browse Files"))
        assertEquals(1, response.size)
        assertTrue( response[0].value.startsWith("References matching 'files' can be found at: "))
    }

    @Test
    fun testModules() {
        val response = browseOperation.handleMessage(message("~browse guava Files"))
        assertEquals(1, response.size)
        assertTrue(response[0].value.startsWith("A reference matching 'files' can be found at: "))
    }

}