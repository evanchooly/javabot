package javabot.dao.impl;

import javabot.Javabot;
import javabot.dao.AbstractDaoImpl;
import javabot.dao.AdminDao;
import javabot.dao.ChannelDao;
import javabot.dao.ConfigDao;
import javabot.model.Admin;
import javabot.model.Channel;
import javabot.model.Config;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created Jun 21, 2007
 *
 * @author <a href="mailto:javabot@cheeseronline.org">cheeser</a>
 */
public class ConfigDaoImpl extends AbstractDaoImpl<Config> implements ConfigDao {
    private static final Logger log = LoggerFactory.getLogger(ConfigDaoImpl.class);
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
        Config config;
        try {
            config = (Config) getEntityManager().createNamedQuery(ConfigDao.GET_CONFIG).getSingleResult();
        } catch (NoResultException e) {
            config = defaults;
            final Channel channel = new Channel();
            channel.setName("##" + config.getNick());
            channelDao.save(channel);
            config.setOperations(Javabot.OPERATIONS);
            adminDao.create(defaultAdmin.getUserName(), defaultAdmin.getHostName());
            save(config);
            props = null;
        }
        return config;
    }
}