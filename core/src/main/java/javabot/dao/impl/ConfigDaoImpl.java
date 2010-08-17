package javabot.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javabot.dao.AbstractDaoImpl;
import javabot.dao.AdminDao;
import javabot.dao.ChannelDao;
import javabot.dao.ConfigDao;
import javabot.model.Admin;
import javabot.model.Channel;
import javabot.model.Config;
import javabot.operations.BotOperation;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created Jun 21, 2007
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class ConfigDaoImpl extends AbstractDaoImpl<Config> implements ConfigDao {
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private Config defaults;
    @Autowired
    private Admin defaultAdmin;

    protected ConfigDaoImpl() {
        super(Config.class);
    }

    public Config get() {
        return (Config) getEntityManager().createNamedQuery(ConfigDao.GET_CONFIG).getSingleResult();
    }

    public Config create() {
        final Config config;
        config = defaults;
        final Channel channel = new Channel();
        channel.setName("##" + config.getNick());
        channelDao.save(channel);
        final List<BotOperation> it = BotOperation.listKnownOperations();
        final List<String> list = new ArrayList<String>();
        for (final BotOperation operation : it) {
            list.add(operation.getClass().getName());
        }
        config.setOperations(list);
        adminDao.create(defaultAdmin.getUserName(), defaultAdmin.getHostName());
        save(config);
        return config;
    }
}