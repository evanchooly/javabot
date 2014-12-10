package javabot.dao;

import com.google.inject.Injector;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import javabot.JavabotConfig;
import javabot.model.Config;
import javabot.model.Logs;
import javabot.model.Persistent;
import javabot.operations.BotOperation;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ConfigDao extends BaseDao<Config> {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigDao.class);

    @Inject
    private Injector injector;

    @Inject
    private JavabotConfig javabotConfig;

    protected ConfigDao() {
        super(Config.class);
    }

    public List<BotOperation> list() {
        Reflections reflections = new Reflections("javabot");

        Set<Class<? extends BotOperation>> classes = reflections.getSubTypesOf(BotOperation.class);

        final List<BotOperation> list = new ArrayList<>();
        for (final Class<? extends BotOperation> operation : classes) {
            if (!Modifier.isAbstract(operation.getModifiers())) {
                try {
                    list.add(injector.getInstance(operation));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
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
        config.setNick(javabotConfig.nick());
        config.setPassword(javabotConfig.password()); // optional
        config.setServer(javabotConfig.ircHost());
        config.setPort(javabotConfig.ircPort());
        config.setTrigger("~");
        for (final BotOperation operation : list()) {
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
}