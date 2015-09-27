package javabot.operations

import javabot.BaseMessagingTest
import org.testng.annotations.Test

@Test(enabled = false)
public class ShunOperationTest : BaseMessagingTest() {
    @Throws(InterruptedException::class)
    public fun shunMe() {
        sendMessage("~forget shunHey")
        try {
            sendMessage("~shunHey is <reply>shunHey")
            scanForResponse("~shun ${testUser} 5", "${testUser} is shunned until")
            testMessage("~shunHey")
            Thread.sleep(5000)
            testMessage("~shunHey", "shunHey")
        } finally {
            sendMessage("~forget shunHey")
        }
    }
}
