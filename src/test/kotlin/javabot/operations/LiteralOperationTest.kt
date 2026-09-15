package javabot.operations

import com.antwerkz.sofia.Sofia
import jakarta.inject.Inject
import java.util.Date
import javabot.BaseTest
import org.testng.Assert
import org.testng.annotations.Test

@Test(groups = arrayOf("operations"))
class LiteralOperationTest : BaseTest() {
    @Inject private lateinit var operation: LiteralOperation

    @Test
    fun testMissingFactoid() {
        val factoidName = "foo${Date().time}"
        var response = operation.handleMessage(message("~literal ${factoidName}"))
        Assert.assertEquals(response[0].value, Sofia.factoidUnknown(factoidName))
    }

    // TODO needs "existing factoid test"
}
