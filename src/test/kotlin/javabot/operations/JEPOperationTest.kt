package javabot.operations

import com.antwerkz.sofia.Sofia
import jakarta.inject.Inject
import javabot.BaseTest
import org.testng.Assert
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test(groups = arrayOf("operations"))
class JEPOperationTest : BaseTest() {
    @Inject protected lateinit var operation: JEPOperation

    @Test
    fun testJEPOperations() {
        val response = operation.handleMessage(message("~jep 220"))
        Assert.assertEquals(
            response[0].value,
            "'JEP 220: Modular Run-Time Images' can be found at " +
                "http://openjdk.java.net/jeps/220"
        )
    }

    @Test
    fun testBadJEPRequest() {
        val response = operation.handleMessage(message("~jep 2202213"))
        Assert.assertEquals(response[0].value, Sofia.jepInvalid("2202213"))
    }

    @DataProvider(name = "badCommands")
    private fun badCommands(): Array<Array<Any>> {
        return arrayOf(
            arrayOf<Any>("~jep", Sofia.jepMissing()),
            arrayOf<Any>("~jep ", Sofia.jepMissing()),
            arrayOf<Any>("~jep abc", Sofia.jepInvalid("abc"))
        )
    }

    @Test(dataProvider = "badCommands")
    fun testNullJEPOperations(command: String, result: String) {
        val response = operation.handleMessage(message(command))
        Assert.assertEquals(response[0].value, result)
    }
}
