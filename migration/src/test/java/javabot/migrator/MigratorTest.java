package javabot.migrator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import javax.inject.Inject;

import com.google.code.morphia.Datastore;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import javabot.JavabotModule;
import javabot.dao.ChannelDao;
import javabot.dao.LogsDao;
import javabot.model.Channel;
import javabot.model.Logs;
import javabot.model.criteria.LogsCriteria;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

@Guice(modules = JavabotModule.class)
public class MigratorTest {
  @Inject
  private LogsDao logsDao;

  @Inject
  private ChannelDao channelDao;

  @Inject
  private Datastore ds;

  @Inject
  private Migrator migrator;

  @Test
  public void verify() throws SQLException {
//    migrator.migrate();
    DBCollection collection = migrator.getDb().getCollection("logsIDs");
    long count = 0;
    DateTime begin = DateTime.now();
    try (Connection connection = migrator.getConnection();
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery("select id from logs")) {
      while (resultSet.next()) {
        count++;
        LogsCriteria criteria = new LogsCriteria(ds);
        long id = resultSet.getLong("id");
        ObjectId objectId = migrator.lookupId("logs", id);
        criteria.id(objectId);
        Assert.assertNotNull(criteria.query().get(),
            String.format("Looking for %d => %s", id, objectId));
        if (count % 10000 == 0) {
          logProgress(count, 4579597, begin);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  private void logProgress(final long count, final long total, final DateTime begin) {
    DateTime current = DateTime.now();
    long duration = current.getMillis() - begin.getMillis();
    int rate = (int) (count / duration) + 1;
    int remaining = (int) ((total - count) / rate);
    DateTime done = current.plusMillis(remaining);
    System.out.printf("Imported %d of %d (%.2f%%).  Estimated completion time: %s\n", count, total,
        100.0 * count / total, done.toString("HH:mm:ss"));
  }

  @Test
  public void logs() {
    Properties props = migrator.getProps();
    ds.getCollection(Logs.class).remove(new BasicDBObject());
    String select = "select * from logs where message='read that book.'";
    try (Connection connection = DriverManager.getConnection(props.getProperty("jdbc.url"),
        props.getProperty("jdbc.user"), props.getProperty("jdbc.password"));
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(select)) {
      if (resultSet.next()) {
        migrator.logs(resultSet);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage(), e);
    }
    String dateString = "2013-05-10";
    DateTime date;
    String pattern = "yyyy-MM-dd";
    DateTimeFormatter format = DateTimeFormat.forPattern(pattern);
    String channelName = "##java";
    if ("today".equals(dateString)) {
      date = DateTime.now().withTimeAtStartOfDay();
    } else {
      try {
        date = DateTime.parse(dateString, format);
      } catch (Exception e) {
        System.out.println("error!");
        date = DateTime.now().withTimeAtStartOfDay();
      }
    }
    scan(channelName, date);
  }

  private void scan(final String channelName, final DateTime date2) {
    List<Logs> logs = dailyLog(channelName, date2);
    for (Logs log : logs) {
      if ("read that book.".equals(log.getMessage())) {
        System.out.println("log.getUpdated() = " + log.getUpdated());
      }
    }
  }

  private List<Logs> dailyLog(String channelName, DateTime date) {
    Channel channel = channelDao.get(channelName);
    List<Logs> list = null;
    System.out.println("dailyLog() date = " + date);
    if (channel.getLogged()) {
      DateTime start = (date == null
          ? new DateTime(DateTimeZone.forID("US/Eastern"))
          : date)
          .withTimeAtStartOfDay();
      DateTime tomorrow = start.plusDays(1);
      LogsCriteria criteria = new LogsCriteria(ds);
      criteria.channel().equal(channelName);
      System.out.println("start = " + start);
      System.out.println("tomorrow = " + tomorrow);
      criteria.and(
          criteria.updated().greaterThanOrEq(start),
          criteria.updated().lessThanOrEq(tomorrow)
      );
      System.out.println("query = " + criteria.query());
      list = criteria.query().asList();
    }
    return list;
  }

}