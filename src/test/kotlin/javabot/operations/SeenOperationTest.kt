package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.BaseMessagingTest
import org.testng.annotations.Test

public class SeenOperationTest : BaseMessagingTest() {
    @Test
    public fun seen() {
        testMessage("~seen jimmyjimjim", Sofia.seenUnknown(testUser.nick, "jimmyjimjim"))
    }
}
