package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.BaseTest
import org.testng.Assert
import org.testng.annotations.Test
import java.util.Date
import javax.inject.Inject

@Test(groups = arrayOf("operations")) class LiteralOperationTest : BaseTest() {
    @Inject
    private lateinit var operation: LiteralOperation
    @Test fun testMissingFactoid() {
        val factoidName = "foo${Date().time}"
        var response = operation.handleMessage(message("literal ${factoidName}"))
        Assert.assertEquals(response[0].value, Sofia.factoidUnknown(factoidName))
    }

    // TODO needs "existing factoid test"
}
