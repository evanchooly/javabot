package javabot

import com.antwerkz.sofia.Sofia
import org.pircbotx.User
import org.testng.Assert

import java.util.Arrays

import java.lang.String.format

public abstract class BaseMessagingTest : BaseTest() {
    protected fun scanForResponse(message: String, target: String) {
        val list = sendMessage(message)
        var found = false
        for (response in list) {
            found = found or response.contains(target)
        }
        Assert.assertTrue(found, format("Did not find \n'%s' in \n'%s'", target, list))
    }

    protected fun testMessage(message: String, vararg responses: String) {
        compareResults(sendMessage(message), responses)
    }

    protected fun testMessageAs(user: User, message: String, vararg responses: String) {
        compareResults(sendMessage(user, message), responses)
    }

    private fun compareResults(messages: Messages, responses: Array<out String>) {
        Assert.assertEquals(messages.size(), responses.size(),
              format("Should get expected response count back. \n** expected: \n%s\n** got: \n%s", Arrays.toString(responses), messages))
        for (response in responses) {
            Assert.assertEquals(messages.remove(0), response)
        }
        Assert.assertTrue(messages.isEmpty(), "All responses should be matched.")
    }

    protected fun sendMessage(message: String): Messages {
        return sendMessage(testUser, message)
    }

    protected fun sendMessage(testUser: User, message: String): Messages {
        javabot.get().processMessage(Message(javabotChannel, testUser, message))
        return messages
    }

    protected fun testMessageList(message: String, responses: List<String>) {
        var found = false
        for (response in sendMessage(message)) {
            found = found or responses.contains(response)
        }
        Assert.assertTrue(found,
              format("Should get one response from the list of possibilities\n** expected: \n%s\n** got: \n%s", responses,
                    sendMessage(message)))
    }

    protected fun getFoundMessage(factoid: String, value: String): String {
        return format("%s, %s is %s", testUser, factoid, value)
    }

    protected fun forgetFactoid(name: String) {
        testMessage(format("~forget %s", name), Sofia.factoidForgotten(name, testUser.nick))
    }
}