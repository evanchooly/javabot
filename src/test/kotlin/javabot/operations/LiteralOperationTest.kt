package javabot.operations

import javabot.BaseMessagingTest
import org.testng.annotations.Test

import java.util.Date

@Test(groups = arrayOf("operations"))
public class LiteralOperationTest : BaseMessagingTest() {
    @Test
    public fun testMissingFactoid() {
        val factoidName = "foo" + Date().time
        testMessage("~literal " + factoidName,
              "I have no factoid called \"" + factoidName + "\"")
    }

    // TODO needs "existing factoid test"
}
