package javabot.dao;

import com.antwerkz.sofia.Sofia;
import javabot.BaseTest;
import javabot.model.Channel;
import javabot.model.Logs;
import javabot.model.Logs.Type;
import javabot.model.UserFactory;
import javabot.model.criteria.LogsCriteria;
import org.mongodb.morphia.Datastore;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.inject.Provider;
import java.time.LocalDateTime;
import java.util.List;

public class LogsDaoTest extends BaseTest {
    @Inject
    private LogsDao logsDao;
    @Inject
    private ChannelDao channelDao;
    @Inject
    private Datastore ds;
    @Inject
    private UserFactory userFactory;
    @Inject
    private Provider<PircBotX> ircBot;

    public static final String CHANNEL_NAME = "#watercooler";

    @Test
    public void seen() {
        LogsCriteria logsCriteria = new LogsCriteria(ds);
        logsCriteria.channel(CHANNEL_NAME);
        logsCriteria.delete();
        channelDao.delete(channelDao.get(CHANNEL_NAME));
        Channel channel = new Channel();
        channel.setName(CHANNEL_NAME);
        channel.setLogged(true);
        channelDao.save(channel);
        logsDao.logMessage(Type.MESSAGE, ircBot.get().getUserChannelDao().getChannel(channel.getName()),
                       userFactory.createUser("ChattyCathy", "ChattyCathy", "localhost"), "test message");

        Assert.assertNotNull(logsDao.getSeen(channel.getName(), "chattycathy"));
        Assert.assertFalse(logsDao.findByChannel(channel.getName(), LocalDateTime.now(), false).isEmpty());
        Assert.assertTrue(logsDao.findByChannel(channel.getName(), LocalDateTime.now().minusDays(1), false).isEmpty());
    }

    @Test
    public void channelEvents() {
        String chanName = "##testChannel";
        channelDao.delete(chanName);
        channelDao.create(chanName, true, null);
        logsDao.deleteAllForChannel(chanName);

        final PircBotX bot = ircBot.get();
        org.pircbotx.Channel channel = bot.getUserChannelDao().getChannel(chanName);
        User user = bot.getUserChannelDao().getUser(getTestUser().getNick());

        logsDao.logMessage(Logs.Type.PART, channel, user, Sofia.userParted(user.getNick()));

        List<Logs> logs = logsDao.findByChannel(chanName, LocalDateTime.now(), true);

        Assert.assertFalse(logs.isEmpty(), "Should have one log entry");
        Assert.assertEquals(logs.get(0).getMessage(), Sofia.userParted(user.getNick()));
    }
}
