package javabot

import com.google.inject.Inject
import org.testng.Assert
import org.testng.annotations.Test

public class JavabotConfigTest : BaseTest() {

    @Inject
    private val javabotConfig: JavabotConfig? = null

    @Test
    public fun testConfig() {
        Assert.assertNotEquals(javabotConfig!!.nick(), "javabot")
        Assert.assertNotEquals(javabotConfig.databaseName(), "javabot")
    }
}