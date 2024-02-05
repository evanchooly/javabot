package javabot.operations

import com.google.inject.Inject
import javabot.BaseTest
import javabot.Javabot.Companion.LOG
import javabot.JavabotConfig
import org.testng.Assert.assertTrue
import org.testng.annotations.Test

class ChatGPTOperationTest : BaseTest() {
    @Inject protected lateinit var operation: ChatGPTOperation

    @Inject protected lateinit var config: JavabotConfig

    @Test
    fun testNonJavaQuestion() {
        if (config.chatGptKey().isNotEmpty()) {
            val response =
                operation.handleMessage(message("~gpt speed of an african laden swallow"))
            assertTrue(response.isEmpty())
            LOG.info("ChatGPT inappropriate (non-java) content test passed")
        } else {
            LOG.info("ChatGPT testing skipped, no key configured")
        }
    }

    @Test
    fun testMavenDirectories() {
        if (config.chatGptKey().isNotEmpty()) {
            val response =
                operation.handleMessage(message("~gpt what is the maven directory structure"))
            assertTrue(response.isNotEmpty())
            assertTrue(response[0].value.contains("src/main/java"))
            LOG.info("ChatGPT appropriate content passed: ${response[0].value}")
        } else {
            LOG.info("ChatGPT testing skipped, no key configured")
        }
    }
}
