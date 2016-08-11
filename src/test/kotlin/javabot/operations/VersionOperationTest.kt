package javabot.operations

import com.google.inject.Inject
import javabot.BaseTest
import org.testng.annotations.Test
import java.util.*

class VersionOperationTest : BaseTest() {
    @Inject
    private lateinit var operation: VersionOperation

    /** We expect the test to not be able to find the manifest resource. */
    @Test
    fun tellVersion() {
        val props= Properties()
        val input=props.javaClass.getResourceAsStream("/version.properties")
        props.load(input)
        val version=props.getProperty("build.version", "UNKNOWN")
        scanForResponse(operation.handleMessage(message("~version")), "I am currently running from git tag "+version)
    }
}