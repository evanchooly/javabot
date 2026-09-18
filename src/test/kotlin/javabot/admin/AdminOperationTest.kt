package javabot.admin

import io.quarkus.test.junit.QuarkusTest
import javabot.BaseTest
import javabot.commands.AdminCommand
import javabot.commands.DisableOperation
import javabot.commands.EnableOperation
import javabot.commands.ListOperations
import javabot.operations.BotOperation
import javabot.operations.StandardOperation
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@QuarkusTest
@TestMethodOrder(OrderAnnotation::class)
class AdminOperationTest : BaseTest() {

    companion object {
        private var disableOperationsSucceeded = false
    }

    // ListOperations/DisableOperation/EnableOperation are Guice-domain (javabot.commands.**
    // is excluded from CDI) -- not real @Inject sites under @QuarkusTest.
    private val listOperation: ListOperations by lazy {
        injector.getInstance(ListOperations::class.java)
    }
    private val disableOperation: DisableOperation by lazy {
        injector.getInstance(DisableOperation::class.java)
    }
    private val enableOperation: EnableOperation by lazy {
        injector.getInstance(EnableOperation::class.java)
    }

    @Test
    @Order(1)
    fun disableOperations() {
        val responses = listOperation.handleMessage(message("~admin listOperations"))
        try {
            for (name in responses[2].value.split(",")) {
                val opName = name.trim().split(" ")[0].trim()
                disableOperation.handleMessage(message("~admin disableOperation -name=${opName}"))

                val operation = findOperation(opName)
                assertTrue(
                    operation == null ||
                        operation is AdminCommand ||
                        operation is StandardOperation,
                    "${opName} should be disabled",
                )
            }
        } finally {
            enableAllOperations()
        }
        disableOperationsSucceeded = true
    }

    @Test
    @Order(2)
    fun enableOperations() {
        assumeTrue(disableOperationsSucceeded, "disableOperations must pass first")
        disableAllOperations()
        val allOperations = bot.get().getAllOperations()
        for ((key) in allOperations) {
            enableOperation.handleMessage(message("~admin enableOperation --name=${key}"))
        }
    }

    private fun findOperation(name: String): BotOperation? {
        return bot.get().activeOperations.filter { op -> op.getName() == name }.firstOrNull()
    }
}
