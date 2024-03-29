package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.BaseTest
import javax.inject.Inject
import org.testng.Assert
import org.testng.annotations.Test

class SeenOperationTest : BaseTest() {
    @Inject private lateinit var operation: SeenOperation

    @Test
    fun seen() {
        var response = operation.handleMessage(message("~seen jimmyjimjim"))
        Assert.assertEquals(response[0].value, Sofia.seenUnknown(TEST_USER.nick, "jimmyjimjim"))
    }
}
