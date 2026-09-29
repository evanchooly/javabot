package javabot

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

@QuarkusTest
class JavabotConfigTest : BaseTest() {

    @Inject private lateinit var javabotConfig: JavabotConfig

    @Test
    fun testConfig() {
        assertNotEquals("javabot", javabotConfig.nick())
        assertNotEquals("javabot", javabotConfig.databaseName())
    }
}
