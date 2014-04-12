package utils;

import java.util.Collections;
import java.util.List;

import javabot.model.Channel;
import javabot.model.Logs;
import javabot.model.criteria.LogsCriteria;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class LogsDao extends javabot.dao.LogsDao {
  private List<Logs> dailyLog(String channelName, DateTime date, Boolean logged) {
    List<Logs> list = null;
    if (logged) {
      DateTime start = ((date == null)
          ? new DateTime(DateTimeZone.forID("US/Eastern"))
          : date).withTimeAtStartOfDay();
      DateTime tomorrow = start.plusDays(1);
      LogsCriteria criteria = new LogsCriteria(ds);
      criteria.channel(channelName);
      criteria.and(
          criteria.updated().lessThanOrEq(tomorrow),
          criteria.updated().greaterThanOrEq(start)
      );
      list = criteria.query().asList();
    }
    return list;
  }

  public List<Logs> findByChannel(String name,DateTime date,Boolean showAll) {
    Channel channel = channelDao.get(name);
    if (channel != null && (showAll || channel.getLogged())) {
      return dailyLog(name, date, showAll || channel.getLogged());
    } else {
      return Collections.emptyList();
    }
  }
}
