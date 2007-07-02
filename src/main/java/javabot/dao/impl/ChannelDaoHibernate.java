package javabot.dao.impl;

import java.util.Date;
import java.util.List;

import javabot.dao.AbstractDaoHibernate;
import javabot.dao.ChannelDao;
import javabot.dao.ConfigDao;
import javabot.dao.util.QueryParam;
import javabot.model.Channel;
import javabot.model.Config;

public class ChannelDaoHibernate extends AbstractDaoHibernate<Channel> implements ChannelDao {
    private ConfigDao configDao;

    public ChannelDaoHibernate() {
        super(Channel.class);
    }

    @SuppressWarnings({"unchecked"})
    public List<String> configuredChannels() {
        return (List<String>)getEntityManager().createNamedQuery(CONFIGURED_CHANNELS).getResultList();
    }

    @SuppressWarnings({"unchecked"})
    public List<Channel> getChannels() {
        return (List<Channel>)getEntityManager().createNamedQuery(ChannelDao.ALL).getResultList();
    }

    @SuppressWarnings({"unchecked"})
    public List<Channel> find(QueryParam qp) {
        StringBuilder query = new StringBuilder("from Channel c");
        if(qp.hasSort()) {
            query.append(" order by ")
                .append(qp.getSort())
                .append((qp.isSortAsc()) ? " asc" : " desc");
        }
        return getEntityManager().createQuery(query.toString())
            .setFirstResult(qp.getFirst())
            .setMaxResults(qp.getCount()).getResultList();
    }

    public Channel get(String name) {
        return (Channel)getEntityManager().createNamedQuery(ChannelDao.BY_NAME)
            .setParameter("channel", name)
            .getSingleResult();

    }

    public Channel create(String name) {
        Channel channel = new Channel();
        Config config = configDao.get();
        channel.setName(name);
        channel.setConfig(config);
        config.getChannels().add(channel);
        save(channel);
        configDao.save(config);
        return channel;
    }

    @Override
    public void save(Channel channel) {
        channel.setUpdated(new Date());
        super.save(channel);
    }

    public ConfigDao getConfigDao() {
        return configDao;
    }

    public void setConfigDao(ConfigDao dao) {
        configDao = dao;
    }
}