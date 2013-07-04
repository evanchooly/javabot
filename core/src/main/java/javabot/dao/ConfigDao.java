package javabot.dao;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import javax.inject.Inject;

import javabot.model.Config;
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
    if(config == null) {
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
    final Set<String> list = new TreeSet<String>();
    for (final BotOperation operation : it) {
      list.add(operation.getName());
    }
    config.setOperations(list);
    save(config);
    return config;
  }

  private String getProperty(String name) {
    final String value = System.getProperty(name);
    if (value == null) {
      throw new RuntimeException("Missing default configuration property: " + name);
    }
    return value;
  }

}
