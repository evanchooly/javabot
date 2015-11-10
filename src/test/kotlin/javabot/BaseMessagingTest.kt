package javabot

import com.antwerkz.sofia.Sofia
import org.pircbotx.User
import org.testng.Assert

import java.util.Arrays

import java.lang.String.format

@Deprecated("this class is no longer necessary", replaceWith = ReplaceWith("BaseTest"), level = DeprecationLevel.WARNING)
public abstract class BaseMessagingTest : BaseTest() {

    @Deprecated("call the operations directly", level = DeprecationLevel.WARNING)
    protected fun testMessage(message: String, vararg responses: String) {
        compareResults(sendMessage(message), responses)
    }

    @Deprecated("call the operations directly", level = DeprecationLevel.WARNING)
    protected fun testMessageAs(user: User, message: String, vararg responses: String) {
        compareResults(sendMessage(user, message), responses)
    }

    @Deprecated("call the operations directly", level = DeprecationLevel.WARNING)
    private fun compareResults(messages: Messages, responses: Array<out String>) {
        Assert.assertEquals(messages.size(), responses.size(),
              format("Should get expected response count back. \n** expected: \n%s\n** got: \n%s", Arrays.toString(responses), messages))
        for (response in responses) {
            Assert.assertEquals(messages.remove(0), response)
        }
        Assert.assertTrue(messages.isEmpty(), "All responses should be matched.")
    }

    @Deprecated("call the operations directly", level = DeprecationLevel.WARNING)
    protected fun sendMessage(message: String): Messages {
        return sendMessage(testUser, message)
    }

    @Deprecated("call the operations directly", level = DeprecationLevel.WARNING)
    protected fun sendMessage(testUser: User, message: String): Messages {
        bot.get().processMessage(Message(testChannel, testUser, message))
        return messages
    }

    @Deprecated("call the operations directly", level = DeprecationLevel.WARNING)
    protected fun testMessageList(message: String, responses: List<String>) {
        var found = false
        for (response in sendMessage(message)) {
            found = found or responses.contains(response)
        }
        Assert.assertTrue(found,
              format("Should get one response from the list of possibilities\n** expected: \n%s\n** got: \n%s", responses,
                    sendMessage(message)))
    }

    @Deprecated("call the operations directly", level = DeprecationLevel.WARNING)
    protected fun getFoundMessage(factoid: String, value: String): String {
        return format("${testUser}, ${factoid} is ${value}")
    }

    @Deprecated("call the operations directly", level = DeprecationLevel.WARNING)
    protected fun forgetFactoid(name: String) {
        testMessage(format("~forget %s", name), Sofia.factoidForgotten(name, testUser.nick))
    }
}