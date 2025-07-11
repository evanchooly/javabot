package javabot.operations

import com.google.inject.Inject
import javabot.BaseTest
import javabot.Javabot.Companion.LOG
import javabot.JavabotConfig
import javabot.NoOperationMessage
import org.testng.Assert.assertTrue
import org.testng.annotations.BeforeClass
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class ChatGPTOperationTest : BaseTest() {
    @Inject protected lateinit var operation: ChatGPTOperation
    @Inject protected lateinit var addFactoidOperation: AddFactoidOperation

    @Inject protected lateinit var config: JavabotConfig

    @BeforeClass
    fun prepFactoids() {
        addFactoidOperation.handleMessage(
            message(
                """
            ~suffering-oriented programming is 
            <reply>Suffering-oriented programming: 
            make it work, make it pretty, make it fast 
            - in that order. 
            http://nathanmarz.com/blog/suffering-oriented-programming.html"""
                    .cleanForIRC()
            )
        )
    }

    @DataProvider
    fun queries() =
        arrayOf(
            arrayOf<Any>("help", false, "query. Note that GPT"),
            arrayOf<Any>("speed of an african laden swallow", true, ""),
            arrayOf<Any>(
                "what is the maven directory structure",
                false,
                "Maven directory structure",
            ),
            arrayOf<Any>("suffering-oriented programming", false, "Suffering-oriented programming"),
            arrayOf<Any>("list of DI frameworks", false, "Spring"),
            arrayOf<Any>("list of DI frameworks", false, "Spring"),
            arrayOf<Any>("how do I declare a new variable in Javascript", true, ""),
        )

    @Test(dataProvider = "queries")
    fun runTestQuery(prompt: String, empty: Boolean, match: String) {
        if (config.chatGptKey().isNotEmpty()) {
            val response = operation.handleMessage(message("~gpt $prompt"))
            println(response)
            if (empty) {
                assertTrue(response.isNotEmpty())
                assertTrue(response[0] is NoOperationMessage)
            } else {
                assertTrue(response.isNotEmpty())
                assertTrue(response[0].value.lowercase().contains(match.lowercase()))
            }
        } else {
            LOG.info("ChatGPT testing skipped, no key configured")
        }
    }

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

    fun testSuffering() {
        val prompt = "~gpt what is suffering-oriented programming?"
        if (config.chatGptKey().isNotEmpty()) {
            val response =
                operation.handleMessage(message("~gpt speed of an african laden swallow"))
            assertTrue(response.isEmpty())
            LOG.info("ChatGPT inappropriate (non-java) content test passed")
        } else {
            LOG.info("ChatGPT testing skipped, no key configured")
        }
    }
}
