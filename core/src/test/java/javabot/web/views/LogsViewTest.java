package javabot.web.views;

import com.antwerkz.sofia.Sofia;
import javabot.dao.ChannelDao;
import javabot.dao.LogsDao;
import javabot.model.Channel;
import javabot.model.Logs.Type;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.IOException;
import java.time.LocalDateTime;

public class LogsViewTest extends ViewsTest {
    @Inject
    private LogsDao logsDao;
    @Inject
    private ChannelDao channelDao;
    @Inject
    private Provider<PircBotX> ircBot;

    @Test
    public void render() throws IOException {
        render(new LogsView(getInjector(), new MockServletRequest(false), "testchannel", LocalDateTime.now()));
    }

    @Test
    public void actions() throws IOException {
        String message = "my type is " + Type.MESSAGE;
        final String eventChannel = "testchannel";
        final User user = getTestUser();

        logsDao.deleteAllForChannel(eventChannel);

        create(Type.MESSAGE, eventChannel, message);

        String action = "really loves deleting boiler plate code";
        create(Type.ACTION, eventChannel, action);
        create(Type.JOIN, eventChannel, Sofia.userJoined(user.getNick(), user.getHostmask(), eventChannel));
        create(Type.QUIT, eventChannel, Sofia.userQuit(user.getNick(), eventChannel));
        create(Type.PART, eventChannel, Sofia.userParted(user.getNick(), "i'm done"));

        String rendered = render(new LogsView(getInjector(), new MockServletRequest(false), eventChannel, LocalDateTime.now())).toString();

        Assert.assertTrue(rendered.contains("<td>" + message + "</td>"),
                          "Should find basic message: \n" + rendered);
        Assert.assertTrue(rendered.contains(">" + user + " " + action + "</td>"),
                          "Should find action: \n" + rendered);
        Assert.assertTrue(rendered.contains(">" + Sofia.userJoined(user.getNick(), user.getHostmask(), eventChannel) + "</td>"),
                          "Should find join: \n" + rendered);
        Assert.assertTrue(rendered.contains(">" + Sofia.userQuit(user.getNick(), eventChannel) + "</td>"),
                          "Should find quit: \n" + rendered);
        Assert.assertTrue(rendered.contains(">" + Sofia.userParted(user.getNick(), "i'm done") + "</td>"),
                          "Should find part: \n" + rendered);
    }

    private void create(final Type type, final String channelName, final String value) {
        Channel channel = channelDao.get(channelName);
        if (channel == null) {
            channelDao.create(channelName, true, null);
        }

        logsDao.logMessage(type, getIrcBot().getUserChannelDao().getChannel(channelName), getTestUser(), value);
    }
}