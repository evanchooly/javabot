package javabot.operations

import javabot.BaseTest
import javabot.Message
import org.testng.annotations.Test
import javax.inject.Inject

@Test(groups = arrayOf("operations"))
class VersionOperationTest : BaseTest() {
    @Inject
    lateinit private var operation: VersionOperation

    @Test
    fun workingVersionTest() {
        val results = operation.handleMessage(message("~version"))

        assert(results.size==1)
        assert(results[0].value.startsWith("I am currently running git tag"))
    }
}