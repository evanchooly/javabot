package javabot.qtest.operations

import io.quarkus.test.junit.QuarkusTest
import java.util.stream.Stream
import javabot.BaseTest
import javabot.Javabot.Companion.LOG
import javabot.JavabotConfig
import javabot.NoOperationMessage
import javabot.operations.AddFactoidOperation
import javabot.operations.ChatGPTOperation
import javabot.operations.cleanForIRC
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@QuarkusTest
class ChatGPTOperationTest : BaseTest() {
    private val operation: ChatGPTOperation by lazy {
        injector.getInstance(ChatGPTOperation::class.java)
    }
    private val addFactoidOperation: AddFactoidOperation by lazy {
        injector.getInstance(AddFactoidOperation::class.java)
    }
    private val config: JavabotConfig by lazy { injector.getInstance(JavabotConfig::class.java) }

    @BeforeEach
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

    companion object {
        @JvmStatic
        fun queries(): Stream<Arguments> =
            Stream.of(
                Arguments.of("help", false, "query. Note that GPT"),
                Arguments.of("speed of an african laden swallow", true, ""),
                Arguments.of(
                    "what is the maven directory structure",
                    false,
                    "Maven directory structure",
                ),
                Arguments.of(
                    "suffering-oriented programming",
                    false,
                    "Suffering-oriented programming",
                ),
                Arguments.of("list of DI frameworks", false, "Spring"),
                Arguments.of("list of DI frameworks", false, "Spring"),
                Arguments.of("how do I declare a new variable in Javascript", true, ""),
            )
    }

    @ParameterizedTest
    @MethodSource("queries")
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

    @Test
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
