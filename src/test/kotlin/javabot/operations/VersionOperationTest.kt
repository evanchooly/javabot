package javabot.operations

import javabot.BaseTest
import javax.inject.Inject
import org.testng.annotations.Test

@Test(groups = arrayOf("operations"))
class VersionOperationTest : BaseTest() {
    @Inject lateinit private var operation: VersionOperation

    @Test
    fun workingVersionTest() {
        val results = operation.handleMessage(message("~version"))

        assert(results.size == 1)
        assert(results[0].value.startsWith("I am currently running git tag"))
    }
}
