package javabot.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.PersistenceException;

import javabot.dao.AbstractDaoHibernate;
import javabot.dao.ChannelDao;
import javabot.dao.ConfigDao;
import javabot.dao.util.QueryParam;
import javabot.model.Channel;
import javabot.model.Config;
import javabot.model.Persistent;
import org.springframework.beans.factory.annotation.Autowired;

public class ChannelDaoHibernate extends AbstractDaoHibernate<Channel> implements ChannelDao {
    @Autowired
    private ConfigDao configDao;
    private Map<String, Boolean> logCache = new HashMap<String, Boolean>();

    public ChannelDaoHibernate() {
        super(Channel.class);
    }

    @Override
    public void delete(Persistent persistedObject) {
        Channel channel = (Channel) getEntityManager().merge(persistedObject);
        channel.getConfig().getChannels().remove(channel);
        channel.setConfig(null);
        super.delete(channel);
    }

    @Override
    public void delete(Long id) {
        delete(find(id));
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<String> configuredChannels() {
        return (List<String>) getEntityManager().createNamedQuery(CONFIGURED_CHANNELS).getResultList();
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<Channel> getChannels() {
        return (List<Channel>) getEntityManager().createNamedQuery(ChannelDao.ALL).getResultList();
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<Channel> find(QueryParam qp) {
        StringBuilder query = new StringBuilder("from Channel c");
        if (qp.hasSort()) {
            query.append(" order by ")
                    .append(qp.getSort())
                    .append(qp.isSortAsc() ? " asc" : " desc");
        }
        return getEntityManager().createQuery(query.toString())
                .setFirstResult(qp.getFirst())
                .setMaxResults(qp.getCount()).getResultList();
    }

    @Override
    public boolean isLogged(String channel) {
        Boolean logged = logCache.get(channel);
        if(logged == null) {
            Channel chan = get(channel);
            if(channel != null) {
                logged = chan.getLogged();
                logCache.put(channel, logged);
            } else {
                logged = Boolean.FALSE;
            }
        }

        return logged;
    }

    @Override
    public Channel get(String name) {
        Channel channel = null;
        try {
            channel = (Channel) getEntityManager().createNamedQuery(ChannelDao.BY_NAME)
                .setParameter("channel", name)
                .getSingleResult();
            return channel;
        } catch (PersistenceException e) {
            // ignore
        }
        return channel;

    }

    @Override
    public Channel create(String name, Boolean logged, String key) {
        Channel channel = new Channel();
        channel.setName(name);
        channel.setLogged(logged);
        channel.setKey(key);
        Config config = configDao.get();
        channel.setConfig(config);
        config.getChannels().add(channel);
        save(channel);
        configDao.save(config);
        return channel;
    }

    @Override
    public void save(Channel channel) {
        channel.setUpdated(new Date());
        merge(channel);
    }

    public ConfigDao getConfigDao() {
        return configDao;
    }

    public void setConfigDao(ConfigDao dao) {
        configDao = dao;
    }
}