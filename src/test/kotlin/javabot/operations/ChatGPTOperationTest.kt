package javabot.operations

import com.google.inject.Inject
import javabot.BaseTest
import javabot.JavabotConfig
import org.testng.Assert.assertTrue
import org.testng.annotations.Test

class ChatGPTOperationTest : BaseTest() {
    @Inject
    protected lateinit var operation: ChatGPTOperation

    @Inject
    protected lateinit var config: JavabotConfig

    @Test
    fun testSwallow() {
        if (config.chatGptKey().isNotEmpty()) {
            val response = operation.handleMessage(message("~gpt speed of an african laden swallow"))
            assertTrue(response.isEmpty())
        }
    }

    @Test
    fun testMavenDirectories() {
        if (config.chatGptKey().isNotEmpty()) {
            val response = operation.handleMessage(message("~gpt what is the maven directory structure"))
            assertTrue(response.isNotEmpty())
            assertTrue(response[0].value.contains("src/main/java"))
        }
    }
}
