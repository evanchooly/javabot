package javabot.operations

import com.google.inject.Inject
import javabot.BaseTest
import org.testng.annotations.Test

class VersionOperationTest : BaseTest() {
    @Inject
    private lateinit var operation: VersionOperation

    /** We expect the test to not be able to find the manifest resource. */
    @Test
    fun tellVersion() {
        scanForResponse(operation.handleMessage(message("~version")), "I am currently running from git tag foobarbaz")
    }
}