package javabot.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import javabot.model.Config;
import javabot.model.Logs;
import javabot.model.Persistent;
import javabot.operations.BotOperation;

/**
 * Created Jun 21, 2007
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class ConfigDao extends BaseDao<Config> {
  String GET_CONFIG = "Config.get";

  @Inject
  private ChannelDao channelDao;

  @Inject
  private AdminDao adminDao;

  @Inject
  private Properties properties;

  protected ConfigDao() {
    super(Config.class);
  }

  public Config get() {
    Config config = ds.createQuery(Config.class).get();
    if (config == null) {
      config = create();
    }
    return config;
  }

  public Config create() {
    final Config config = new Config();
    config.setNick(properties.getProperty("javabot.nick"));
    config.setPassword(properties.getProperty("javabot.password")); // optional
    config.setServer(properties.getProperty("javabot.server", "irc.freenode.org"));
    config.setPort(Integer.parseInt(properties.getProperty("javabot.port", "6667")));
    config.setTrigger("~");
    final List<BotOperation> it = BotOperation.list();
    final List<String> list = new ArrayList<>();
    for (final BotOperation operation : it) {
      list.add(operation.getName());
    }
    config.setOperations(list);
    updateHistoryIndex(config.getHistoryLength());
    save(config);
    return config;
  }

  @Override
  public void save(final Persistent object) {
    if (object instanceof Config && object.getId() != null) {
      Config config = (Config) object;
      Config old = get();
      if (old != null) {
        if (!old.getHistoryLength().equals(config.getHistoryLength())) {
          updateHistoryIndex(config.getHistoryLength());
        }
      }
    }
    super.save(object);
  }

  private void updateHistoryIndex(final Integer historyLength) {
    DBCollection collection = ds.getCollection(Logs.class);
    try {
      collection.dropIndex("updated_1");
    } catch (Exception e) {
      // no such index yet?
    }
    BasicDBObject keys = new BasicDBObject();
    keys.put("expireAfterSeconds", TimeUnit.DAYS.toSeconds(historyLength * 31));
    keys.put("background", Boolean.TRUE);
    try {
      collection.ensureIndex(new BasicDBObject("updated", 1), keys);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  private String getProperty(String name) {
    final String value = System.getProperty(name);
    if (value == null) {
      throw new RuntimeException("Missing default configuration property: " + name);
    }
    return value;
  }
}