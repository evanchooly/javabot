package javabot.migrator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.inject.Inject;

import com.google.code.morphia.Datastore;
import com.mongodb.DBCollection;
import javabot.JavabotModule;
import javabot.dao.ChannelDao;
import javabot.dao.LogsDao;
import javabot.javadoc.criteria.JavadocClassCriteria;
import javabot.model.Channel;
import javabot.model.Logs;
import javabot.model.criteria.LogsCriteria;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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
    DBCollection collection = migrator.getDb().getCollection("classesIDs");
    long count = 0;
    DateTime begin = DateTime.now();
    try (Connection connection = migrator.getConnection();
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery("select id from classes")) {
      while (resultSet.next()) {
        count++;
        long id = resultSet.getLong("id");
        ObjectId objectId = migrator.lookupId("classes", id);

        JavadocClassCriteria criteria = new JavadocClassCriteria(ds);
        criteria.id(objectId);
        Assert.assertNotNull(criteria.query().get(), String.format("Looking for %d => %s", id, objectId));
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

  @Test(dependsOnMethods = {"verify"})
  public void logs() {
    LogsCriteria criteria = new LogsCriteria(ds);
    criteria.message().contains("read that book");
    Assert.assertNotNull(criteria.query().get());
/*
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
*/
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
    if (channel.getLogged()) {
      DateTime start = (date == null
          ? new DateTime(DateTimeZone.forID("US/Eastern"))
          : date)
          .withTimeAtStartOfDay();
      DateTime tomorrow = start.plusDays(1);
      LogsCriteria criteria = new LogsCriteria(ds);
      criteria.channel().equal(channelName);
      criteria.and(
          criteria.updated().greaterThanOrEq(start),
          criteria.updated().lessThanOrEq(tomorrow)
      );
      list = criteria.query().asList();
    }
    return list;
  }

}