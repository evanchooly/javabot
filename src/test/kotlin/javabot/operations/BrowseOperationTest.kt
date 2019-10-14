package javabot.operations

import javabot.BaseTest
import org.testng.annotations.Test
import javax.inject.Inject
import kotlin.test.assertEquals

@Test(groups = arrayOf("operations"))
class BrowseOperationTest @Inject constructor(val browseOperation: BrowseOperation) : BaseTest() {
    @Test
    fun testNonMatchingInput() {
        var response = browseOperation.handleMessage(message("~test pong is pong"))
        assertEquals(0, response.size)
    }

    @Test
    fun testClasses() {
        var response = browseOperation.handleMessage(message("~browse String"))
        println(response)
    }

    @Test
    fun testModules() {
        var response = browseOperation.handleMessage(message("~browse guava String"))
        println(response)
    }

}