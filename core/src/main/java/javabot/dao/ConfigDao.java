package javabot.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import javabot.model.Config;
import javabot.model.Logs;
import javabot.model.Persistent;
import javabot.operations.BotOperation;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ConfigDao extends BaseDao<Config> {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigDao.class);

    @Inject
    private Properties properties;

    private Query<Config> query;

    protected ConfigDao() {
        super(Config.class);
    }

    public Config get() {
        if (query == null) {
            query = ds.createQuery(Config.class);
        }
        Config config = query.get();
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
        for (final BotOperation operation : BotOperation.list()) {
            config.getOperations().add(operation.getName());
        }
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
            collection.createIndex(new BasicDBObject("updated", 1), keys);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
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