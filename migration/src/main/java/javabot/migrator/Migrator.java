package javabot.migrator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

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
import javabot.javadoc.JavadocClass;
import javabot.javadoc.JavadocField;
import javabot.javadoc.JavadocMethod;
import javabot.model.Change;
import javabot.model.Channel;
import javabot.model.Config;
import javabot.model.Factoid;
import javabot.model.Karma;
import javabot.model.Logs;
import javabot.model.NickRegistration;
import javabot.model.Shun;
import org.bson.types.ObjectId;

public class Migrator {
  private Properties props;

  private Connection connection;

  @Inject
  private Mongo mongo;

  @Inject
  private LogsDao logsDao;

  @Inject
  private ApiDao apiDao;

  @Inject
  private JavadocClassDao javadocClassDao;

  private DB db;

  private void changes(final ResultSet resultSet) throws SQLException {
    Change log = new Change();
    log.setId(mapId("changes", resultSet.getLong("id")));
    log.setMessage(resultSet.getString("message"));
    log.setChangeDate(resultSet.getDate("changedate"));
    logsDao.save(log);
  }

  private void channel(final ResultSet resultSet) throws SQLException {
    Channel channel = new Channel();
    channel.setId(mapId("changes", resultSet.getLong("id")));
    channel.setKey(resultSet.getString("key"));
    channel.setLogged(resultSet.getBoolean("logged"));
    channel.setName(resultSet.getString("name"));
    channel.setUpdated(resultSet.getDate("updated"));
    logsDao.save(channel);
  }

  private Object configuration() throws SQLException {
    System.out.println("Migrating configuration");
    Config config = new Config();
    try (Statement statement = connection.createStatement();
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
    } catch(Exception e) {
      e.printStackTrace();
      System.exit(-1);
    }
    try (Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery("select * from configuration_operations")) {
      while (resultSet.next()) {
        config.getOperations().add(resultSet.getString("element"));
      }
    }
    logsDao.save(config);
    System.out.println("Finished migrating configuration");
    return null;
  }

  private void factoids(final ResultSet resultSet) throws SQLException {
    Factoid factoid = new Factoid();
    factoid.setId(mapId("factoids", resultSet.getLong("id")));
    factoid.setLastUsed(resultSet.getDate("lastused"));
    factoid.setName(resultSet.getString("name"));
    factoid.setUpdated(resultSet.getDate("updated"));
    factoid.setUserName(resultSet.getString("username"));
    factoid.setValue(resultSet.getString("value"));
    factoid.setLocked(resultSet.getBoolean("locked"));
    logsDao.save(factoid);
  }

  private void karma(final ResultSet resultSet) throws SQLException {
    Karma karma = new Karma();
    karma.setId(mapId("karma", resultSet.getLong("id")));
    karma.setName(resultSet.getString("name"));
    karma.setUpdated(resultSet.getDate("updated"));
    karma.setUserName(resultSet.getString("username"));
    karma.setValue(resultSet.getInt("value"));
    logsDao.save(karma);
  }

  private void logs(ResultSet resultSet) throws SQLException {
    Logs log = new Logs();
    log.setId(mapId("logs", resultSet.getLong("id")));
    log.setChannel(resultSet.getString("channel"));
    log.setMessage(resultSet.getString("message"));
    log.setNick(resultSet.getString("nick"));
    log.setType(Logs.Type.values()[resultSet.getInt("type")]);
    log.setUpdated(resultSet.getDate("updated"));
    logsDao.save(log);
  }

  private void registrations(final ResultSet resultSet) throws SQLException {
    NickRegistration reg = new NickRegistration();
    reg.setId(mapId("registrations", resultSet.getLong("id")));
    reg.setHost(resultSet.getString("host"));
    reg.setNick(resultSet.getString("nick"));
    reg.setTwitterName(resultSet.getString("twittername"));
    reg.setUrl(resultSet.getString("url"));
    logsDao.save(reg);
  }

  private void shun(final ResultSet resultSet) throws SQLException {
    Shun shun = new Shun();
    shun.setId(mapId("shun", resultSet.getLong("id")));
    shun.setExpiry(resultSet.getDate("expiry"));
    shun.setNick(resultSet.getString("nick"));
    logsDao.save(shun);
  }

  private Object javadoc() throws Exception {
    System.out.println("Migrating javadoc");
    new TableIterator("apis") {
      public void callOut(final ResultSet resultSet) throws SQLException {
        apis(resultSet);
      }
    }.call();
    new ClassIterator().call();
    new TableIterator("methods") {
      public void callOut(final ResultSet resultSet) throws SQLException {
        methods(resultSet);
      }
    }.call();
    new TableIterator("fields") {
      public void callOut(final ResultSet resultSet) throws SQLException {
        fields(resultSet);
      }
    }.call();
    return null;
  }

  private void apis(final ResultSet resultSet) throws SQLException {
    JavadocApi api = new JavadocApi();
    api.setId(mapId("apis", resultSet.getLong("id")));
    api.setBaseUrl(resultSet.getString("baseurl"));
    api.setName(resultSet.getString("name"));
    api.setPackages(resultSet.getString("packages"));
    logsDao.save(api);
  }

  private void methods(final ResultSet resultSet) throws SQLException {
    JavadocMethod method = new JavadocMethod();
    method.setId(mapId("methods", resultSet.getLong("id")));
    method.setLongUrl(resultSet.getString("longurl"));
    method.setShortUrl(resultSet.getString("shorturl"));
    method.setLongSignatureTypes(resultSet.getString("longsignaturestripped"));
    method.setMethodName(resultSet.getString("methodname"));
    method.setParamCount(resultSet.getInt("paramcount"));
    method.setShortSignatureTypes(resultSet.getString("shortsignaturestripped"));
    method.setDirectUrl(resultSet.getString("directurl"));
    ObjectId classId = lookupId("classes", resultSet.getLong("clazz_id"));
    JavadocClass javadocClass = javadocClassDao.find(classId);
    method.setJavadocClass(javadocClass);
    method.setApi(javadocClass.getApi());
    javadocClassDao.save(method);
  }

  private void fields(final ResultSet resultSet) throws SQLException {
    JavadocField field = new JavadocField();
    field.setId(mapId("fields", resultSet.getLong("id")));
    field.setDirectUrl(resultSet.getString("directurl"));
    field.setLongUrl(resultSet.getString("longurl"));
    field.setShortUrl(resultSet.getString("shorturl"));
    field.setName(resultSet.getString("name"));
    field.setType(resultSet.getString("type"));
    ObjectId classId = lookupId("classes", resultSet.getLong("clazz_id"));
    JavadocClass javadocClass = javadocClassDao.find(classId);
    field.setJavadocClass(javadocClass);
    field.setApi(javadocClass.getApi());
    javadocClassDao.save(field);

  }

  private void migrate() throws SQLException {
    validateProperties();
    db = mongo.getDB(props.getProperty("database.name"));
    ExecutorService executorService = Executors.newFixedThreadPool(5);
    connection = DriverManager.getConnection(props.getProperty("jdbc.url"),
        props.getProperty("jdbc.user"), props.getProperty("jdbc.password"));
    try {
      executorService.submit(new TableIterator("changes") {
        public void callOut(final ResultSet resultSet) throws SQLException {
          changes(resultSet);
        }
      });
      executorService.submit(new TableIterator("channel") {
        public void callOut(final ResultSet resultSet) throws SQLException {
          channel(resultSet);
        }
      });
//      executorService.submit(new TableIterator("logs") {
//        public void callOut(final ResultSet resultSet) throws SQLException {
//          logs(resultSet);
//        }
//      });
      executorService.submit(new TableIterator("factoids") {
        public void callOut(final ResultSet resultSet) throws SQLException {
          factoids(resultSet);
        }
      });
      executorService.submit(new TableIterator("karma") {
        public void callOut(final ResultSet resultSet) throws SQLException {
          karma(resultSet);
        }
      });
      executorService.submit(new TableIterator("registrations") {
        public void callOut(final ResultSet resultSet) throws SQLException {
          registrations(resultSet);
        }
      });
      executorService.submit(new TableIterator("shun") {
        public void callOut(final ResultSet resultSet) throws SQLException {
          shun(resultSet);
        }
      });
      executorService.submit(new Callable<Object>() {
        public Object call() throws SQLException {
          return configuration();
        }
      });
      executorService.submit(new Callable<Object>() {
        public Object call() throws Exception {
          return javadoc();
        }
      });
      executorService.shutdown();
      executorService.awaitTermination(1, TimeUnit.HOURS);
    } catch (InterruptedException e) {
      throw new RuntimeException(e.getMessage(), e);
    } finally {
      connection.close();
      executorService.shutdownNow();
    }
  }

  private ObjectId lookupId(final String table, final long id) {
    DBCollection collection = db.getCollection(table + "IDs");
    collection.ensureIndex(new BasicDBObject(table + "_id", "1"));
    DBObject object = collection.findOne(new BasicDBObject(table + "_id", id));
    return object != null ? (ObjectId) object.get("_id") : null;
  }

  private ObjectId mapId(final String table, final long id) {
    DBCollection collection = db.getCollection(table + "IDs");
    ObjectId mappedId = new ObjectId();
    BasicDBObject insert = new BasicDBObject(table + "_id", id);
    insert.put("_id", mappedId);
    collection.insert(insert);
    return mappedId;
  }

  public void validateProperties() {
    props = new Properties();
    try {
      try (InputStream stream = getClass().getResourceAsStream("/javabot.properties")) {
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

  private abstract class TableIterator implements Callable<Object> {
    private static final int BATCH = 1000;

    private static final String SELECT = "select * from %s limit " + BATCH + " offset %s";

    protected String table;

    private String select;

    public TableIterator(final String table) {
      this(table, SELECT);
    }

    public TableIterator(final String table, String select) {
      this.table = table;
      this.select = select;
    }

    @Override
    public Object call() throws Exception {
      try {
        System.out.println("Migrating " + table);
        int start = 0;
        int count = -1;
        while (count != 0) {
          count = 0;
          System.out.printf("Grabbing next %s from %s\n", BATCH, table);
          try (Statement statement = connection.createStatement();
               ResultSet resultSet = statement.executeQuery(String.format(select, table, start))) {
            while (resultSet.next()) {
              count++;
              callOut(resultSet);
            }
          } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
          }
          start += count;
        }
        System.out.println("Finished migrating " + table);
        return null;
      } catch (Throwable e) {
        e.printStackTrace();
        return null;
      }
    }

    public abstract void callOut(final ResultSet resultSet) throws SQLException;
  }

  private class ClassIterator extends TableIterator {
    private final List<Long> deferredList = new ArrayList<>();

    public ClassIterator() {
      super("classes");
    }

    @Override
    public Object call() throws Exception {
      super.call();
      while (!deferredList.isEmpty()) {
        Iterator<Long> iterator = deferredList.iterator();
        while (iterator.hasNext()) {
          final Long aLong = iterator.next();
          if (process(aLong)) {
            iterator.remove();
          }
        }
      }
      return null;
    }

    private boolean process(Long id) {
      try (Statement statement = connection.createStatement();
           ResultSet resultSet = statement.executeQuery(String.format("select superclass_id from"
               + " classes where id = " + id))) {
        if (resultSet.next()) {
          ObjectId parentId = lookupId("classes", resultSet.getLong("superclass_id"));
          if (parentId != null) {
            JavadocClass javadocClass = javadocClassDao.find(lookupId("classes", id));
            javadocClass.setSuperClass(javadocClassDao.find(parentId));
            javadocClassDao.save(javadocClass);
            return true;
          }
        }
      } catch (SQLException e) {
        throw new RuntimeException(e.getMessage(), e);
      }
      return false;
    }

    public void callOut(final ResultSet resultSet) throws SQLException {
      JavadocClass javadocClass = new JavadocClass();
      long id = resultSet.getLong("id");
      javadocClass.setId(mapId(table, id));
      javadocClass.setLongUrl(resultSet.getString("longurl"));
      javadocClass.setShortUrl(resultSet.getString("shorturl"));
      javadocClass.setName(resultSet.getString("classname"));
      javadocClass.setPackageName(resultSet.getString("packagename"));
      setSuperClassId(resultSet, javadocClass, id);
      ObjectId apiId = lookupId("apis", resultSet.getLong("api_id"));
      javadocClass.setApi(apiDao.find(apiId));
      javadocClassDao.save(javadocClass);
    }

    private void setSuperClassId(final ResultSet resultSet, final JavadocClass javadocClass, final long id)
        throws SQLException {
      long parentId = resultSet.getLong("superclass_id");
      if (!resultSet.wasNull()) {
        ObjectId objectId = lookupId(table, parentId);
        if (objectId == null) {
          defer(id);
        } else {
          javadocClass.setSuperClass(javadocClassDao.find(objectId));
        }
      }
    }

    private void defer(final Long id) {
      deferredList.add(id);
    }
  }
}
