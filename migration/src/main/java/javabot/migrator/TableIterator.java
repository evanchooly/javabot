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

  private DB db;

  public TableIterator(final Migrator migrator, final String table) throws SQLException {
    this(migrator, table, SELECT);
  }

  public TableIterator(final Migrator migrator, final String table, String select) throws SQLException {
    this.migrator = migrator;
    this.table = table;
    this.select = select;
    db = migrator.getDb();
    DBCollection collection = db.getCollection(table + "IDs");
    collection.ensureIndex(new BasicDBObject(table + "_id", 1));
  }

  @Override
  public Object call() throws Exception {
    System.out.println("Migrating " + table);
    long count = 0;
    long total = 0;
    DateTime begin = DateTime.now();
    try (Connection connection = migrator.getConnection();
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(String.format(COUNT, table))) {
      while (resultSet.next()) {
        total = resultSet.getLong(1);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage(), e);
    }
    System.out.printf("Found %d items to migrate.\n", total);
    while (count < total) {
      try (Connection connection = migrator.getConnection();
           Statement statement = connection.createStatement();
           ResultSet resultSet = statement.executeQuery(String.format(select, table, count))) {
        long start = System.currentTimeMillis();
        while (resultSet.next()) {
          count++;
          callOut(resultSet);
          if (count % 10000 == 0) {
            logProgress(count, total, begin, start);
            start = System.currentTimeMillis();
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e.getMessage(), e);
      }
    }
    System.out.printf("Finished migrating %s. %s%n", table, new DateTime().toString("HH:mm:ss"));
    count = countResults();
    if (total != count) {
      throw new RuntimeException(String.format("Failed to migrate all records for %s.  Expected %d but got %d",
          table, total, count));
    }
    return null;
  }

  private void logProgress(final long count, final long total, final DateTime begin, final long start) {
    DateTime current = DateTime.now();
    long duration = current.getMillis() - begin.getMillis();
    int rate = (int) (count / (duration + 1));
    int remaining = (int) ((total - count) / (rate + 1));
    DateTime done = current.plusMillis(remaining);
    System.out.printf("Imported %d of %d (%.2f%%) from %s at %s records/s.  Estimated completion time: %s\n", count, total,
        100.0 * count / total, table, (System.currentTimeMillis() - start)/10000.0, done.toString("HH:mm:ss"));
  }

  protected final long countResults() {
    return db.getCollection(table).count();
  }

  public abstract void callOut(final ResultSet resultSet) throws SQLException;
}
