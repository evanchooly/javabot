package javabot.operations

import com.google.inject.Inject
import javabot.BaseMessagingTest
import javabot.BaseTest
import javabot.operations.locator.JCPJSRLocator
import org.testng.Assert
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

import org.testng.Assert.assertNotNull

@Test(groups = arrayOf("operations"))
public class JSROperationTest : BaseTest() {
    @Inject
    protected lateinit var locator: JCPJSRLocator
    @Inject
    protected lateinit var operation: JSROperation

    @Test
    public fun testLocatorConfig() {
        assertNotNull(locator)
    }

    @Test
    public fun testJSROperations() {
        var response = operation.handleMessage(message("jsr 220"))
                Assert.assertEquals(response[0].value, "'JSR 220: Enterprise JavaBeans 3.0' can be found at http://www.jcp" +
                        ".org/en/jsr/detail?id=220")
    }

    @Test
    public fun testBadJSRRequest() {
        var response = operation.handleMessage(message("jsr 2202213"))
        Assert.assertEquals(response[0].value, "I'm sorry, I can't find a JSR 2202213")
    }

    @DataProvider(name = "badCommands")
    private fun badCommands(): Array<Array<Any>> {
        return arrayOf(arrayOf<Any>("~jsr", "Please supply a JSR number to look up."),
              arrayOf<Any>("~jsr ", "Please supply a JSR number to look up."),
              arrayOf<Any>("~jsr abc", "'abc' is not a valid JSR reference."))
    }

    @Test(dataProvider = "badCommands")
    public fun testNullJSROperations(command: String, result: String) {
        var response = operation.handleMessage(message(command))
        Assert.assertEquals(response[0].value, result)
    }

}
