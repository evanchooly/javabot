package javabot.dao;

import java.time.LocalDateTime;
import javax.inject.Inject;

import javabot.BaseTest;
import javabot.model.Channel;
import javabot.model.Logs.Type;
import javabot.model.criteria.LogsCriteria;
import org.junit.Assert;
import org.mongodb.morphia.Datastore;
import org.testng.annotations.Test;

public class LogsDaoTest extends BaseTest {
  @Inject
  private LogsDao dao;
  @Inject
  private ChannelDao channelDao;
  @Inject
  private Datastore ds;

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
    dao.logMessage(Type.MESSAGE, "ChattyCathy", channel.getName(), "test message");

    Assert.assertNotNull(dao.getSeen("chattycathy", channel.getName()));
    Assert.assertFalse(dao.findByChannel(channel.getName(), LocalDateTime.now(), false).isEmpty());
    Assert.assertTrue(dao.findByChannel(channel.getName(), LocalDateTime.now().minusDays(1), false).isEmpty());
  }
}
