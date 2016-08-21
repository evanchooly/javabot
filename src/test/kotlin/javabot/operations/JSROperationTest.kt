package javabot.operations

import com.antwerkz.sofia.Sofia
import com.google.inject.Inject
import javabot.BaseTest
import javabot.operations.locator.JCPJSRLocator
import org.testng.Assert
import org.testng.Assert.assertNotNull
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test(groups = arrayOf("operations")) class JSROperationTest : BaseTest() {
    @Inject
    private lateinit var locator: JCPJSRLocator
    @Inject
    private lateinit var operation: JSROperation

    @Test fun testLocatorConfig() {
        assertNotNull(locator)
    }

    @Test fun testJSROperations() {
        val response = operation.handleMessage(message("~jsr 220"))
        Assert.assertEquals(response[0].value, "'JSR 220: Enterprise JavaBeans 3.0' can be found at http://www.jcp" +
                ".org/en/jsr/detail?id=220")
    }

    @Test fun testBadJSRRequest() {
        val response = operation.handleMessage(message("~jsr 2202213"))
        Assert.assertEquals(response[0].value, Sofia.jsrUnknown("2202213"))
    }

    @DataProvider(name = "badCommands")
    private fun badCommands(): Array<Array<Any>> {
        return arrayOf(arrayOf<Any>("~jsr", Sofia.jsrMissing()),
                arrayOf<Any>("~jsr ", Sofia.jsrMissing()),
                arrayOf<Any>("~jsr abc", Sofia.jsrInvalid("abc")))
    }

    @Test(dataProvider = "badCommands") fun testNullJSROperations(command: String, result: String) {
        val response = operation.handleMessage(message(command))
        Assert.assertEquals(response[0].value, result)
    }

}
