package javabot.migrator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Callable;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import org.joda.time.DateTime;

public abstract class TableIterator implements Callable<Object> {
  public static final int BATCH = 10_000;

  private static final String COUNT = "select count(*) from %s";

  private static final String SELECT = "select * from %s";// limit " + BATCH + " offset %s";

  protected Migrator migrator;

  protected String table;

  private String select;

  private Class klass;

  private DB db;

  private DBCollection collection;

  public TableIterator(final Migrator migrator, final String table, final Class klass) throws SQLException {
    this(migrator, table, SELECT, klass);
  }

  public TableIterator(final Migrator migrator, final String table, String select, final Class klass) {
    this.migrator = migrator;
    this.table = table;
    this.select = select;
    this.klass = klass;
    db = migrator.getDb();
    db.getCollection(table + "IDs").ensureIndex(new BasicDBObject(table + "_id", 1));
    collection = migrator.getDs().getCollection(klass);
  }

  @Override
  public Object call() throws Exception {
    System.out.println("Migrating " + table);
    long count = 0;
    long total = 0;
    try (Connection connection = migrator.getConnection();
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(String.format(COUNT, table))) {
      while (resultSet.next()) {
        total = resultSet.getLong(1);
      }
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
    System.out.printf(" - Found %d items to migrate.\n", total);
    DateTime initialTime = DateTime.now();
    while (count < total) {
      try (Connection connection = migrator.getConnection();
           Statement statement = connection.createStatement();
           ResultSet resultSet = statement.executeQuery(String.format(select, table, count))) {
        while (resultSet.next()) {
          count++;
          migrate(resultSet);
          if (count % 10000 == 0) {
            logProgress(initialTime, total, count);
          }
        }
      } catch (Exception e) {
        throw new RuntimeException(e.getMessage(), e);
      }
    }
    System.out.printf(" * Finished migrating %s. %s%n%n", table, new DateTime().toString("HH:mm:ss"));
    count = countResults();

    if (total != count) {
      throw new RuntimeException(String.format("Failed to migrate all records for %s.  Expected %d but got %d",
          table, total, count));
    }
    return null;
  }

  private void logProgress(final DateTime begin, final long total, final long count) {

    long duration = (DateTime.now().getMillis() - begin.getMillis()) / 1000;

    double rate = duration != 0 ? 1.0 * count / duration : 0;

    long remainingTime = rate != 0 ? (long) ((total - count) / rate) : 0;

    System.out.printf(" - Imported %d of %d (%.2f%%) from %s at %.2f records/s.  Estimated completion time: %s\n", count,
        total, 100.0 * count / total, table, rate, DateTime.now().plusSeconds((int) remainingTime).toString("HH:mm:ss"));
  }

  protected final long countResults() {
    return collection.count();
  }

  public abstract void migrate(final ResultSet resultSet) throws SQLException;
}
