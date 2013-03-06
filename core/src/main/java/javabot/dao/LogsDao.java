package javabot.dao;

import java.util.Collections;
import java.util.Date;
import java.util.List;
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
/*
@NamedQueries({
    @NamedQuery(name = LogsDao.COUNT_LOGGED, query = "select count(s) from Logs s where s.channel like '#%'"),
    @NamedQuery(name = LogsDao.SEEN,
        query = "select new javabot.Seen(l.nick, l.message, l.channel, l.updated) from Logs l where"
            + " lower(l.nick) = :nick AND l.channel = :channel order by l.updated desc")
})
*/

  @SuppressWarnings({"unchecked"})
  public List<Logs> dailyLog(final String channelName, final Date date) {
    Channel channel = channelDao.get(channelName);
    if (channel.getLogged()) {
      DateTime today = new DateTime(date == null ? new Date() : date).toDateMidnight().toDateTime();
      DateTime tomorrow = today.plusDays(1);
      LogsCriteria criteria = new LogsCriteria(ds);
      criteria.channel().equal(channelName);
      criteria.or(
          criteria.updated().greaterThanOrEq(today.toDate()),
          criteria.updated().lessThanOrEq(tomorrow.toDate())
      );
      return criteria.query().asList();
    }
    return Collections.emptyList();
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
    logMessage.setUpdated(new Date());
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
    return new Seen(logs.getChannel(), logs.getMessage(), logs.getNick(), logs.getUpdated());
  }

}