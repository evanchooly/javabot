package javabot.migrator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.Set;
import javax.inject.Inject;

import com.google.code.morphia.Datastore;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import javabot.JavabotModule;
import javabot.dao.ApiDao;
import javabot.dao.JavadocClassDao;
import javabot.dao.LogsDao;
import javabot.javadoc.JavadocApi;
import javabot.model.Change;
import javabot.model.Channel;
import javabot.model.Config;
import javabot.model.Factoid;
import javabot.model.Karma;
import javabot.model.Logs;
import javabot.model.NickRegistration;
import javabot.model.Shun;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;

public class Migrator {
  private Properties props;

  @Inject
  private Mongo mongo;

  @Inject
  private Datastore ds;

  @Inject
  private LogsDao logsDao;

  @Inject
  private ApiDao apiDao;

  @Inject
  private JavadocClassDao javadocClassDao;

  private DB db;

  public Migrator() {
    validateProperties();
  }

  public Properties getProps() {
    return props;
  }

  private void changes(final ResultSet resultSet) throws SQLException {
    Change log = new Change();
    log.setMessage(resultSet.getString("message"));
    log.setChangeDate(extractDate(resultSet, "changedate"));
    logsDao.save(log);
  }

  private void channel(final ResultSet resultSet) throws SQLException {
    Channel channel = new Channel();
    channel.setKey(resultSet.getString("key"));
    channel.setLogged(resultSet.getBoolean("logged"));
    channel.setName(resultSet.getString("name"));
    channel.setUpdated(extractDate(resultSet, "updated"));
    logsDao.save(channel);
  }

  private Object configuration() throws SQLException {
    System.out.println("Migrating configuration");
    Config config = new Config();
    try (Connection connection = getConnection();
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery("select * from configuration")) {
      if (resultSet.next()) {
        config.setId(new ObjectId());
        config.setHistoryLength(resultSet.getInt("historylength"));
        config.setNick(resultSet.getString("nick"));
        config.setPassword(resultSet.getString("password"));
        config.setPort(resultSet.getInt("port"));
        config.setServer(resultSet.getString("server"));
        config.setSchemaVersion(resultSet.getInt("schemaversion"));
        config.setTrigger(resultSet.getString("trigger"));
        config.setUrl(resultSet.getString("url"));
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(-1);
    }
    try (Connection connection = getConnection();
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery("select * from configuration_operations")) {
      while (resultSet.next()) {
        config.getOperations().add(resultSet.getString("element"));
      }
    }
    logsDao.save(config);
    System.out.println(" * Finished migrating configuration\n");
    return null;
  }

  private void factoids(final ResultSet resultSet) throws SQLException {
    Factoid factoid = new Factoid();
    factoid.setLastUsed(extractDate(resultSet, "lastused"));
    factoid.setName(resultSet.getString("name"));
    factoid.setUpdated(extractDate(resultSet, "updated"));
    factoid.setUserName(resultSet.getString("username"));
    factoid.setValue(resultSet.getString("value"));
    factoid.setLocked(resultSet.getBoolean("locked"));
    logsDao.save(factoid);
  }

  private void karma(final ResultSet resultSet) throws SQLException {
    Karma karma = new Karma();
    karma.setName(resultSet.getString("name"));
    karma.setUpdated(extractDate(resultSet, "updated"));
    karma.setUserName(resultSet.getString("username"));
    karma.setValue(resultSet.getInt("value"));
    logsDao.save(karma);
  }

  protected void logs(ResultSet resultSet) throws SQLException {
    Logs log = new Logs();
    log.setChannel(resultSet.getString("channel"));
    log.setMessage(resultSet.getString("message"));
    log.setNick(resultSet.getString("nick"));
    log.setType(Logs.Type.values()[resultSet.getInt("type")]);
    log.setUpdated(extractDate(resultSet, "updated"));
    logsDao.save(log);
  }

  private DateTime extractDate(final ResultSet resultSet, final String column) throws SQLException {
    Timestamp timestamp = resultSet.getTimestamp(column);
    return resultSet.wasNull() ? null : new DateTime(timestamp.getTime());
  }

  private void registrations(final ResultSet resultSet) throws SQLException {
    NickRegistration reg = new NickRegistration();
    reg.setHost(resultSet.getString("host"));
    reg.setNick(resultSet.getString("nick"));
    reg.setTwitterName(resultSet.getString("twittername"));
    reg.setUrl(resultSet.getString("url"));
    logsDao.save(reg);
  }

  private void shun(final ResultSet resultSet) throws SQLException {
    Shun shun = new Shun();
    shun.setExpiry(extractDate(resultSet, "expiry"));
    shun.setNick(resultSet.getString("nick"));
    logsDao.save(shun);
  }

  private Object javadoc() throws Exception {
    new TableIterator(this, "apis") {
      public void migrate(final ResultSet resultSet) throws SQLException {
        apis(resultSet);
      }
    }.call();
    new ClassIterator(this, javadocClassDao, apiDao).call();
    new MethodIterator(this, javadocClassDao).call();
    new FieldIterator(this, javadocClassDao).call();
    return null;
  }

  private void apis(final ResultSet resultSet) throws SQLException {
    JavadocApi api = new JavadocApi();
    api.setId(mapId("apis", resultSet.getLong("id")));
    api.setBaseUrl(resultSet.getString("baseurl"));
    api.setName(resultSet.getString("name"));
    logsDao.save(api);
  }

  public void migrate() throws SQLException {
    clearCollections();
    try {
      new TableIterator(this, "changes") {
        public void migrate(final ResultSet resultSet) throws SQLException {
          changes(resultSet);
        }
      }.call();
      new TableIterator(this, "channel") {
        public void migrate(final ResultSet resultSet) throws SQLException {
          channel(resultSet);
        }
      }.call();
      new TableIterator(this, "factoids") {
        public void migrate(final ResultSet resultSet) throws SQLException {
          factoids(resultSet);
        }
      }.call();
      new TableIterator(this, "karma") {
        public void migrate(final ResultSet resultSet) throws SQLException {
          karma(resultSet);
        }
      }.call();
      new TableIterator(this, "registrations") {
        public void migrate(final ResultSet resultSet) throws SQLException {
          registrations(resultSet);
        }
      }.call();
      new TableIterator(this, "shun") {
        public void migrate(final ResultSet resultSet) throws SQLException {
          shun(resultSet);
        }
      }.call();
      configuration();
      javadoc();
      new TableIterator(this, "logs", "select * from %s order by updated desc limit 100000 offset %s") {
        public void migrate(final ResultSet resultSet) throws SQLException {
          logs(resultSet);
        }
      }.call();
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
    System.out.println("Recreating indexes");
    ds.ensureIndexes(true);
  }

  private void clearCollections() {
    Set<String> collectionNames = getDb().getCollectionNames();
    for (String name : collectionNames) {
      if (!"system.indexes".equals(name)) {
        System.out.println("Removing " + name);
        getDb().getCollection(name).drop();
      }
    }
  }

  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(props.getProperty("jdbc.url"),
        props.getProperty("jdbc.user"), props.getProperty("jdbc.password"));
  }

  protected ObjectId lookupId(final String table, final long id) {
    DBCollection collection = getDb().getCollection(table + "IDs");
    DBObject object = collection.findOne(new BasicDBObject(table + "_id", id));
    return object != null ? (ObjectId) object.get("_id") : null;
  }

  protected ObjectId mapId(final String table, final long id) {
    DBCollection collection = getDb().getCollection(table + "IDs");
    ObjectId mappedId = new ObjectId();
    BasicDBObject insert = new BasicDBObject(table + "_id", id);
    insert.put("_id", mappedId);
    collection.insert(insert);
    return mappedId;
  }

  public DB getDb() {
    if (db == null) {
      db = mongo.getDB(props.getProperty("database.name"));
    }
    return db;
  }

  public final void validateProperties() {
    props = new Properties();
    try {
      try (InputStream stream = new FileInputStream("javabot.properties")) {
        props.load(stream);
      } catch (FileNotFoundException e) {
        throw new RuntimeException("Please define a javabot.properties file to configure the bot");
      }
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
    check(props, "database.host");
    check(props, "database.port");
    check(props, "database.name");
    check(props, "jdbc.url");
    System.getProperties().putAll(props);
  }

  private void check(final Properties props, final String key) {
    if (props.get(key) == null) {
      throw new RuntimeException(String.format("Please specify the property %s in javabot.properties", key));
    }
  }

  public static void main(String[] args) {
    Injector injector = Guice.createInjector(new JavabotModule());
    try {
      injector.getInstance(Migrator.class).migrate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    System.exit(0);
  }

}
