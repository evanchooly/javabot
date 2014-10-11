package javabot.dao;

import javabot.BaseTest;
import javabot.model.Channel;
import javabot.model.Logs.Type;
import javabot.model.UserFactory;
import javabot.model.criteria.LogsCriteria;
import org.junit.Assert;
import org.mongodb.morphia.Datastore;
import org.pircbotx.PircBotX;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.time.LocalDateTime;

public class LogsDaoTest extends BaseTest {
    @Inject
    private LogsDao dao;
    @Inject
    private ChannelDao channelDao;
    @Inject
    private Datastore ds;
    @Inject
    private UserFactory userFactory;
    @Inject
    private PircBotX ircBot;

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
        dao.logMessage(Type.MESSAGE, ircBot.getUserChannelDao().getChannel(channel.getName()),
                       userFactory.createUser("ChattyCathy", "ChattyCathy", "localhost"), "test message");

        Assert.assertNotNull(dao.getSeen(channel.getName(), "chattycathy"));
        Assert.assertFalse(dao.findByChannel(channel.getName(), LocalDateTime.now(), false).isEmpty());
        Assert.assertTrue(dao.findByChannel(channel.getName(), LocalDateTime.now().minusDays(1), false).isEmpty());
    }
}
