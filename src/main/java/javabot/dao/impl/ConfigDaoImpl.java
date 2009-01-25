package javabot.dao.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.persistence.NoResultException;

import javabot.ApplicationException;
import javabot.dao.AbstractDaoImpl;
import javabot.dao.AdminDao;
import javabot.dao.ChannelDao;
import javabot.dao.ConfigDao;
import javabot.model.Channel;
import javabot.model.Config;
import javabot.operations.AdminOperation;
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
    private Properties props;

    protected ConfigDaoImpl() {
        super(Config.class);
    }

    public Config get() {
        Config config;
        try {
            config = (Config) getEntityManager().createNamedQuery(ConfigDao.GET_CONFIG).getSingleResult();
        } catch (NoResultException e) {
            config = new Config();
            config.setNick(getProperty("javabot.nick", true));
            config.setPassword(getProperty("javabot.password", true));
            config.setServer(getProperty("javabot.server", "irc.freenode.net", true));
            config.setPrefixes(config.getNick());
            final String port = getProperty("javabot.port", false);
            if (port != null) {
                config.setPort(Integer.parseInt(port));
            }
            final Channel channel = new Channel();
            channel.setName("##" + config.getNick());
            channelDao.save(channel);
            config.setOperations(AdminOperation.OPERATIONS);
            adminDao.create(getProperty("javabot.admin.nick", true), getProperty("javabot.admin.hostmask", true));
            save(config);
            props = null;
        }
        return config;
    }

    private String getProperty(final String key, final boolean required) {
        return getProperty(key, null, required);
    }

    private String getProperty(final String key, final String defaultValue, final boolean required) {
        loadProperties();
        final String value = props.getProperty(key, defaultValue);
        if (required && value == null) {
            throw new ApplicationException("No configuration found and missing property " + key);
        }
        return value;
    }

    private void loadProperties() {
        if (props == null) {
            props = new Properties();
            try {
                InputStream inStream = null;
                try {
                    inStream = new FileInputStream("javabot.properties");
                    if (inStream != null) {
                        props.load(inStream);
                        inStream.close();
                    }
                    inStream = getClass().getResourceAsStream("/locations-override.properties");
                    if (inStream != null) {
                        props.load(inStream);
                    }
                } finally {
                    if (inStream != null) {
                        inStream.close();
                    }
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}