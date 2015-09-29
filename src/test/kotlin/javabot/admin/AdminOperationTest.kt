package javabot.admin

import com.jayway.awaitility.Awaitility
import javabot.BaseMessagingTest
import javabot.commands.AdminCommand
import javabot.operations.BotOperation
import javabot.operations.StandardOperation
import org.testng.Assert
import org.testng.annotations.Test
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MILLISECONDS

public class AdminOperationTest : BaseMessagingTest() {

    @Test
    public fun disableOperations() {
        val messages = sendMessage("~admin listOperations")
        try {
            for (name in messages.get(2).split(",")) {
                val opName = name.trim().split(" ")[0].trim()
                sendMessage("~admin disableOperation -name=${opName}")
                Awaitility.await("~admin disableOperation -name=${opName}")
                      .pollInterval(100, MILLISECONDS)
                      .atMost(20, TimeUnit.SECONDS)
                      .until<Boolean> {
                          val operation = findOperation(opName)
                          operation == null || operation is AdminCommand || operation is StandardOperation
                      }

                val operation = findOperation(opName)
                Assert.assertTrue(operation == null || operation is AdminCommand || operation is StandardOperation,
                      "${opName} should be disabled")
            }
        } finally {
            enableAllOperations()
        }
    }

    private fun findOperation(name: String): BotOperation? {
        return javabot.get().activeOperations
              .filter { op -> op.getName() == name }
              .firstOrNull()
    }

    @Test(dependsOnMethods = arrayOf("disableOperations"))
    public fun enableOperations() {
        disableAllOperations()
        val allOperations = javabot.get().getAllOperations()
        for (entry in allOperations.entrySet()) {
            val opName = entry.getKey()
            sendMessage("~admin enableOperation --name=${opName}")
            Awaitility.await("~admin enableOperation --name=${opName}")
                  .atMost(60, TimeUnit.SECONDS)
                  .until<Boolean> {
                      findOperation(opName) != null
                  }
            messages.get()
        }
    }
}
