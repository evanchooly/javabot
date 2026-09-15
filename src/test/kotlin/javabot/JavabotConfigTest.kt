package javabot

import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

@QuarkusTest
class JavabotConfigTest : BaseTest() {

    private val javabotConfig: JavabotConfig by lazy {
        injector.getInstance(JavabotConfig::class.java)
    }

    @Test
    fun testConfig() {
        assertNotEquals("javabot", javabotConfig.nick())
        assertNotEquals("javabot", javabotConfig.databaseName())
    }
}
