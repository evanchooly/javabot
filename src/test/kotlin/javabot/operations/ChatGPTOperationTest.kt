package javabot.operations

import com.google.inject.Inject
import javabot.BaseTest
import org.testng.Assert.assertTrue
import org.testng.annotations.Test

class ChatGPTOperationTest : BaseTest() {
    @Inject
    protected lateinit var operation: ChatGPTOperation

    @Test
    fun testSwallow() {
        val response = operation.handleMessage(message("~gpt speed of an african laden swallow"))
        assertTrue(response.isEmpty())
    }
    @Test
    fun testMavenDirectories() {
        val response = operation.handleMessage(message("~gpt what is the maven directory structure"))
        assertTrue(response.isNotEmpty())
        assertTrue(response[0].value.contains("src/main/java"))
    }
}
