package javabot.web.views;

import javabot.dao.ChannelDao;
import javabot.dao.LogsDao;
import javabot.model.Channel;
import javabot.model.Logs.Type;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import org.pircbotx.PircBotX;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.IOException;
import java.time.LocalDateTime;

import static java.lang.String.format;

public class LogsViewTest extends ViewsTest {
    @Inject
    private LogsDao logsDao;
    @Inject
    private ChannelDao channelDao;
    @Inject
    private Provider<PircBotX> ircBot;

    @Test
    public void render() throws IOException {
        Source source = render(new LogsView(getInjector(), new MockServletRequest(false), "testchannel", LocalDateTime.now()));
    }

    @Test
    public void actions() throws IOException {
        String value = "my type is " + Type.MESSAGE;
        create(Type.MESSAGE, "testchannel", value);
        String rendered = render(new LogsView(getInjector(), new MockServletRequest(false), "testchannel", LocalDateTime.now())).toString();
        Assert.assertTrue(rendered.contains("<td>" + value + "</td>"), "Should find basic message");

        String action = "really loves deleting boiler plate code";
        create(Type.ACTION, "testchannel", action);
        Source source = render(new LogsView(getInjector(), new MockServletRequest(false), "testchannel", LocalDateTime.now()));
        Assert.assertNotNull(source.getFirstElement("class", "action", false), "Should find action");

        value = format(":unaffiliated/%s joined the channel", getTestUser());
        create(Type.JOIN, "testchannel", value);
        source = render(new LogsView(getInjector(), new MockServletRequest(false), "testchannel", LocalDateTime.now()));
        Assert.assertNotNull(source.getFirstElement("class", "join", false), "Should find join");

        value = format("%s quit", getTestUser());
        create(Type.QUIT, "testchannel", value);
        source = render(new LogsView(getInjector(), new MockServletRequest(false), "testchannel", LocalDateTime.now()));
        Assert.assertNotNull(source.getFirstElement("class", "quit", false), "Should find quit");

        value = format(":unaffiliated/%s parted the channel", getTestUser());
        create(Type.PART, "testchannel", value);
        source = render(new LogsView(getInjector(), new MockServletRequest(false), "testchannel", LocalDateTime.now()));
        Assert.assertNotNull(source.getFirstElement("class", "part", false), "Should find part");
    }

    private void create(final Type type, final String channelName, final String value) {
        logsDao.deleteAllForChannel(channelName);
        Channel channel = channelDao.get(channelName);
        if(channel == null) {
            channelDao.create(channelName, true, null);
        }

        logsDao.logMessage(type, getIrcBot().getUserChannelDao().getChannel(channelName), getTestUser(), value);
    }
}