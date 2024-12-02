package javabot

import jakarta.inject.Inject
import org.testng.Assert
import org.testng.annotations.Test

class JavabotConfigTest : BaseTest() {

    @Inject protected lateinit var javabotConfig: JavabotConfig

    @Test
    fun testConfig() {
        Assert.assertNotEquals(javabotConfig.nick(), "javabot")
        Assert.assertNotEquals(javabotConfig.databaseName(), "javabot")
    }
}
