package javabot.dao.impl;

import javabot.model.Config;
import javabot.dao.AbstractDaoHibernate;
import javabot.dao.ConfigDao;

/**
 * Created Jun 21, 2007
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class ConfigHibernateDao extends AbstractDaoHibernate<Config> implements ConfigDao {
    protected ConfigHibernateDao() {
        super(Config.class);
    }

    public Config get() {
        Config config = (Config)getSession().getNamedQuery(ConfigDao.GET_CONFIG).uniqueResult();
        if(config == null) {
            config = new Config();
            save(config);
        }
        return config;
    }
}
