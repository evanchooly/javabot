package javabot.dao.impl;

import javax.persistence.NoResultException;

import javabot.dao.AbstractDaoHibernate;
import javabot.dao.ConfigDao;
import javabot.model.Config;

/**
 * Created Jun 21, 2007
 *
 * @author <a href="mailto:javabot@cheeseronline.org">cheeser</a>
 */
public class ConfigHibernateDao extends AbstractDaoHibernate<Config> implements ConfigDao {
    protected ConfigHibernateDao() {
        super(Config.class);
    }

    public Config get() {
        Config config;
        try {
            config = (Config) getEntityManager().createNamedQuery(ConfigDao.GET_CONFIG).getSingleResult();
        } catch(NoResultException e) {
            config = new Config();
            save(config);
        }
        return config;
    }
}