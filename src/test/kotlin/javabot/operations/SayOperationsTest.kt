package javabot.operations

import javabot.BaseMessagingTest
import org.testng.annotations.Test

@Test(groups = arrayOf("operations"))
public class SayOperationsTest : BaseMessagingTest() {
    public fun testSay() {
        testMessage("~say MAGNIFICENT", "MAGNIFICENT")
    }
}