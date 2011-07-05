package javabot.dao.impl;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javabot.dao.AbstractDaoImpl;
import javabot.dao.AdminDao;
import javabot.dao.ChannelDao;
import javabot.dao.ConfigDao;
import javabot.model.Config;
import javabot.operations.BotOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created Jun 21, 2007
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@Component
public class ConfigDaoImpl extends AbstractDaoImpl<Config> implements ConfigDao {
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private AdminDao adminDao;

    protected ConfigDaoImpl() {
        super(Config.class);
    }

    public Config get() {
        return (Config) getEntityManager().createNamedQuery(ConfigDao.GET_CONFIG).getSingleResult();
    }

    public Config create() {
        final Config config = new Config();
        config.setNick(System.getProperty("javabot.nick"));
        config.setPassword(System.getProperty("javabot.password")); // optional
        config.setServer(System.getProperty("javabot.server", "irc.freenode.org"));
        config.setPort(Integer.parseInt(System.getProperty("javabot.port", "6667")));
        config.setTrigger("~");

        final List<BotOperation> it = BotOperation.list();
        final Set<String> list = new TreeSet<String>();
        for (final BotOperation operation : it) {
            list.add(operation.getName());
        }
        config.setOperations(list);
        adminDao.create(getProperty("javabot.admin.nick"), getProperty("javabot.admin.hostmask"));
        save(config);
        return config;
    }

    private String getProperty(String name) {
        final String value = System.getProperty(name);
        if(value == null) {
            throw new RuntimeException("Missing default configuration property: " + name);
        }
        return value;
    }
}