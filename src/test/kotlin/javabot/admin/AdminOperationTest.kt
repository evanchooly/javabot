package javabot.admin

import com.jayway.awaitility.Awaitility
import javabot.BaseMessagingTest
import javabot.Messages
import javabot.commands.AdminCommand
import javabot.operations.BotOperation
import javabot.operations.StandardOperation
import org.testng.Assert
import org.testng.annotations.Test

import javax.inject.Inject
import java.util.concurrent.TimeUnit

@Test
public class AdminOperationTest : BaseMessagingTest() {

    public fun disableOperations() {
        val messages = sendMessage("~admin listOperations")
        try {
            for (name in messages.get(2).split(",")) {
                val opName = name.trim().split(" ")[0].trim()
                sendMessage("~admin disableOperation --name=" + opName)
                val operation = findOperation(opName)
                Assert.assertTrue(operation == null || operation is AdminCommand || operation is StandardOperation,
                      opName + " should be disabled")
            }
        } finally {
            enableAllOperations()
        }
    }

    public fun findOperation(name: String): BotOperation? {
        for (op in javabot.get().activeOperations) {
            if (op.getName() == name) {
                return op
            }
        }
        return null
    }

    @Test(dependsOnMethods = arrayOf("disableOperations"))
    public fun enableOperations() {
        disableAllOperations()
        val allOperations = javabot.get().getAllOperations()
        for (entry in allOperations.entrySet()) {
            val opName = entry.getKey()
            sendMessage("~admin enableOperation --name=" + opName)
            Awaitility.await("~admin enableOperation --name=" + opName).atMost(60, TimeUnit.SECONDS).until<Any> {
                findOperation(opName) != null
            }
            messages.get()
        }
    }
}
