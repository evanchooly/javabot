package javabot.operations

import com.google.inject.Inject
import javabot.BaseMessagingTest
import javabot.operations.locator.JCPJSRLocator
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

import org.testng.Assert.assertNotNull

@Test(groups = arrayOf("operations"))
public class JSROperationTest : BaseMessagingTest() {
    @Inject
    protected lateinit var locator: JCPJSRLocator

    @Test
    public fun testLocatorConfig() {
        assertNotNull(locator)
    }

    @Test
    public fun testJSROperations() {
        testMessage("~jsr 220", "'JSR 220: Enterprise JavaBeans 3.0' " + "can be found at http://www.jcp.org/en/jsr/detail?id=220")
    }

    @Test
    public fun testBadJSRRequest() {
        testMessage("~jsr 2202213", "I'm sorry, I can't find a JSR 2202213")
    }

    @DataProvider(name = "badCommands")
    private fun badCommands(): Array<Array<Any>> {
        return arrayOf(arrayOf<Any>("~jsr", "Please supply a JSR number to look up."),
              arrayOf<Any>("~jsr ", "Please supply a JSR number to look up."),
              arrayOf<Any>("~jsr abc", "'abc' is not a valid JSR reference."))
    }

    @Test(dataProvider = "badCommands")
    public fun testNullJSROperations(command: String, result: String) {
        testMessage(command, result)
    }

}
