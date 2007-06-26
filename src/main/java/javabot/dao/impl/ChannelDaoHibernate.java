package javabot.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javabot.dao.AbstractDaoHibernate;
import javabot.dao.ChannelDao;
import javabot.dao.ConfigDao;
import javabot.dao.util.QueryParam;
import javabot.model.Channel;
import javabot.model.Config;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ChannelDaoHibernate extends AbstractDaoHibernate<Channel> implements ChannelDao {
    private ConfigDao configDao;

    public ChannelDaoHibernate() {
        super(Channel.class);
    }

    @SuppressWarnings({"unchecked"})
    public List<String> configuredChannels() {
        String query = "select distinct s.channel from Channel s";
        List<String> m_channels = (List<String>)getSession().createQuery(query).list();
        if(m_channels == null) {
            return new ArrayList<String>();
        }
        return m_channels;
    }

    @SuppressWarnings({"unchecked"})
    public List<Channel> getChannels() {
        return (List<Channel>)getSession().getNamedQuery(ChannelDao.ALL).list();
    }

    @SuppressWarnings({"unchecked"})
    public Iterator<Channel> getIterator(QueryParam qp) {
        StringBuilder query = new StringBuilder("from Channel c");
        if(qp.hasSort()) {
            query.append(" order by ")
                .append(qp.getSort())
                .append((qp.isSortAsc()) ? " asc" : " desc");
        }
        return getSession().createQuery(query.toString())
            .setFirstResult(qp.getFirst())
            .setMaxResults(qp.getCount()).iterate();
    }

    public Channel get(String name) {
        Channel channel = (Channel)getSession().getNamedQuery(ChannelDao.BY_NAME)
            .setString("channel", name)
            .uniqueResult();
        if(channel == null) {
            channel = new Channel();
            Config config = configDao.get();
            channel.setConfig(config);
//            config.getChannels().add(channel);
//            configDao.saveOrUpdate(config);
            saveOrUpdate(channel);
        }
        return channel;

    }

    public Channel getChannel(String name) {
        return get(name);
    }

    private boolean isChannel(String channel) {
        return get(channel).getName() != null;
    }

    public void saveOrUpdate(Channel channel) {
        channel.setUpdated(new Date());
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(channel);
        transaction.commit();
    }

    public ConfigDao getConfigDao() {
        return configDao;
    }

    public void setConfigDao(ConfigDao dao) {
        configDao = dao;
    }
}