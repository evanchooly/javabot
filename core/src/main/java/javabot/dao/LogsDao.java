package javabot.dao;

import javax.inject.Inject;

import javabot.Seen;
import javabot.model.Channel;
import javabot.model.Logs;
import javabot.model.criteria.LogsCriteria;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogsDao extends BaseDao<Logs> {
  public static final String TODAY = "Logs.today";
  public static final String COUNT_LOGGED = "Logs.countLogged";
  public static final String SEEN = "Logs.seen";
  public static final Logger log = LoggerFactory.getLogger(LogsDao.class);
  @Inject
  public ConfigDao dao;
  @Inject
  public ChannelDao channelDao;

  public LogsDao() {
    super(Logs.class);
  }

  public void logMessage(final Logs.Type type, final String nick, final String channel,
      final String message) {
    final Channel chan = channel != null ? channelDao.get(channel) : null;
    final Logs logMessage = new Logs();
    logMessage.setType(type);
    logMessage.setNick(nick);
    if (channel != null) {
      logMessage.setChannel(channel.toLowerCase());
    }
    logMessage.setMessage(message);
    logMessage.setUpdated(new DateTime());
    save(logMessage);
  }

  public boolean isSeen(final String nick, final String channel) {
    return getSeen(nick, channel) != null;
  }

  public Seen getSeen(final String nick, final String channel) {
    LogsCriteria criteria = new LogsCriteria(ds);
    criteria.upperNick().equal(nick.toUpperCase());
    criteria.channel().equal(channel);
    Logs logs = criteria.query().get();
    return logs != null ? new Seen(logs.getChannel(), logs.getMessage(), logs.getNick(), logs.getUpdated()) : null;
  }

}