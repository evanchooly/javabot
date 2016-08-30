package javabot.web.views

import com.antwerkz.sofia.Sofia
import javabot.model.Logs.Type
import org.testng.Assert
import org.testng.annotations.Test
import java.time.LocalDateTime

class LogsViewTest : ViewsTest() {

    @Test
    fun render() {
        render(viewFactory.createLogsView(MockServletRequest(false), "testchannel", LocalDateTime.now()))
    }

    @Test
    fun actions() {
        val message = "my type is " + Type.MESSAGE
        val eventChannel = "testchannel"
        val user = TEST_USER

        logsDao.deleteAllForChannel(eventChannel)

        create(Type.MESSAGE, eventChannel, message)
        create(Type.MESSAGE, eventChannel, "this is a test of a url: http://google.com/ now isn't that cool")

        val action = "really loves deleting boiler plate code"
        create(Type.ACTION, eventChannel, action)
        create(Type.JOIN, eventChannel, Sofia.userJoined(user.nick, user.hostmask, eventChannel))
        create(Type.QUIT, eventChannel, Sofia.userQuit(user.nick, eventChannel))
        create(Type.PART, eventChannel, Sofia.userParted(user.nick, "i'm done"))

        val rendered = render(viewFactory.createLogsView(MockServletRequest(false), eventChannel, LocalDateTime.now())).toString()

        Assert.assertTrue(rendered.contains(Sofia.logsAnchorFormat("http://google.com/", "http://google.com/")),
              "Should find url in logs: \n" + Sofia.logsAnchorFormat("http://google.com/", "http://google.com/"))
        Assert.assertTrue(rendered.contains("<td>$message</td>"),
              "Should find basic message: \n" + rendered)
        Assert.assertTrue(rendered.contains(">$user $action</td>"),
              "Should find action: \n" + rendered)
        Assert.assertTrue(rendered.contains(">" + Sofia.userJoined(user.nick, user.hostmask, eventChannel) + "</td>"),
              "Should find join: \n" + rendered)
        Assert.assertTrue(rendered.contains(">" + Sofia.userQuit(user.nick, eventChannel) + "</td>"),
              "Should find quit: \n" + rendered)
        Assert.assertTrue(rendered.contains(">" + Sofia.userParted(user.nick, "i'm done") + "</td>"),
              "Should find part: \n" + rendered)
    }

    private fun create(type: Type, channelName: String, value: String) {
        var channel = channelDao.get(channelName)
        if (channel == null) {
            channel = channelDao.create(channelName, true, null)
        }

        logsDao.logMessage(type, channel, TEST_USER, value)
    }
}