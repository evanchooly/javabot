package javabot.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

import javabot.Seen;
import javabot.model.Channel;
import javabot.model.Logs;
import javabot.model.criteria.LogsCriteria;
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
    logMessage.setUpdated(LocalDateTime.now());
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

  private List<Logs> dailyLog(String channelName, LocalDateTime date, Boolean logged) {
    List<Logs> list = null;
    if (logged) {
      LocalDate localDateTime = date == null
          ? LocalDate.now()
          : date.toLocalDate();
      LocalDate start = localDateTime;
      LocalDate tomorrow = start.plusDays(1);
      LogsCriteria criteria = new LogsCriteria(ds);
      criteria.channel(channelName);
      LocalDateTime nextMidnight = tomorrow.atStartOfDay();
      LocalDateTime lastMidnight = start.atStartOfDay();
      criteria.and(
          criteria.updated().lessThanOrEq(nextMidnight),
          criteria.updated().greaterThanOrEq(lastMidnight)
      );
      list = criteria.query().asList();
    }
    return list;
  }

  public List<Logs> findByChannel(String name,LocalDateTime date,Boolean showAll) {
    Channel channel = channelDao.get(name);
    if (channel != null && (showAll || channel.getLogged())) {
      return dailyLog(name, date, showAll || channel.getLogged());
    } else {
      return Collections.emptyList();
    }
  }

}