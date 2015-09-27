package javabot.web.views

import com.antwerkz.sofia.Sofia
import javabot.dao.ChannelDao
import javabot.dao.LogsDao
import javabot.kotlin.web.views.LogsView
import javabot.model.Channel
import javabot.model.Logs.Type
import org.pircbotx.PircBotX
import org.pircbotx.User
import org.testng.Assert
import org.testng.annotations.Test

import javax.inject.Inject
import javax.inject.Provider
import java.io.IOException
import java.time.LocalDateTime

public class LogsViewTest : ViewsTest() {

    @Test
    @Throws(IOException::class)
    public fun render() {
        render(LogsView(injector, MockServletRequest(false), "testchannel", LocalDateTime.now()))
    }

    @Test
    @Throws(IOException::class)
    public fun actions() {
        val message = "my type is " + Type.MESSAGE
        val eventChannel = "testchannel"
        val user = testUser

        logsDao.deleteAllForChannel(eventChannel)

        create(Type.MESSAGE, eventChannel, message)
        create(Type.MESSAGE, eventChannel, "this is a test of a url: http://google.com/ now isn't that cool")

        val action = "really loves deleting boiler plate code"
        create(Type.ACTION, eventChannel, action)
        create(Type.JOIN, eventChannel, Sofia.userJoined(user.nick, user.hostmask, eventChannel))
        create(Type.QUIT, eventChannel, Sofia.userQuit(user.nick, eventChannel))
        create(Type.PART, eventChannel, Sofia.userParted(user.nick, "i'm done"))

        val rendered = render(LogsView(injector, MockServletRequest(false), eventChannel, LocalDateTime.now())).toString()

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
        val channel = channelDao.get(channelName)
        if (channel == null) {
            channelDao.create(channelName, true, null)
        }

        logsDao.logMessage(type, getIrcBot().userChannelDao.getChannel(channelName), testUser, value)
    }
}