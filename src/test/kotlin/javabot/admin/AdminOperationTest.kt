package javabot.admin

import javabot.BaseTest
import javabot.commands.AdminCommand
import javabot.commands.DisableOperation
import javabot.commands.EnableOperation
import javabot.commands.ListOperations
import javabot.operations.BotOperation
import javabot.operations.StandardOperation
import org.testng.Assert
import org.testng.annotations.Test
import javax.inject.Inject

class AdminOperationTest: BaseTest() {

    @Inject lateinit var listOperation: ListOperations
    @Inject lateinit var disableOperation: DisableOperation
    @Inject lateinit var enableOperation: EnableOperation

    @Test fun disableOperations() {
        val responses = listOperation.handleMessage(message("~admin listOperations"))
        try {
            for (name in responses[2].value.split(",")) {
                val opName = name.trim().split(" ")[0].trim()
                disableOperation.handleMessage(message("~admin disableOperation -name=${opName}"))

                val operation = findOperation(opName)
                Assert.assertTrue(operation == null || operation is AdminCommand || operation is StandardOperation,
                      "${opName} should be disabled")
            }
        } finally {
            enableAllOperations()
        }
    }

    @Test(dependsOnMethods = arrayOf("disableOperations"))
    fun enableOperations() {
        disableAllOperations()
        val allOperations = bot.get().getAllOperations()
        for ((key) in allOperations) {
            enableOperation.handleMessage(message("~admin enableOperation --name=${key}"))
        }
    }

    private fun findOperation(name: String): BotOperation? {
        return bot.get().activeOperations
              .filter { op -> op.getName() == name }
              .firstOrNull()
    }
}
