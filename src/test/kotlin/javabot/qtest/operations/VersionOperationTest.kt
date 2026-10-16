package javabot.qtest.operations

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import javabot.BaseTest
import javabot.operations.VersionOperation
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@QuarkusTest
class VersionOperationTest : BaseTest() {
    @Inject private lateinit var operation: VersionOperation

    @Test
    @Tag("operations")
    fun workingVersionTest() {
        val results = operation.handleMessage(message("~version"))

        assert(results.size == 1)
        assert(results[0].value.startsWith("I am currently running git tag"))
    }
}
