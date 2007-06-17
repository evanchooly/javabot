package javabot.dao;

import javabot.dao.model.Channel;
import javabot.dao.util.QueryParam;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ChannelDaoHibernate extends AbstractDaoHibernate<Channel> implements ChannelDao {

    private static final Log log = LogFactory.getLog(ChannelDaoHibernate.class);

    public ChannelDaoHibernate() {
        super(Channel.class);
    }

    @SuppressWarnings({"unchecked"})
    public List<String> configuredChannels() {
        String query = "select distinct s.channel from Channel s";

        List<String> m_channels = (List<String>) getSession().createQuery(query).list();

        if (m_channels == null) {
            return new ArrayList<String>();
        }

        return m_channels;
    }

    @SuppressWarnings({"unchecked"})
    public List<Channel> getChannels() {

        String query = "From Channel c";

        List<Channel> channels = getSession().createQuery(query).list();

        Collections.sort(channels, new Comparator<Channel>() {
            public int compare(Channel channel, Channel channel1) {
                return channel.getChannel().compareTo(channel1.getChannel());
            }
        });

        return channels;
    }

    @SuppressWarnings({"unchecked"})
    public Iterator<Channel> getIterator(QueryParam qp) {
        StringBuilder query = new StringBuilder("from Channel c");

        if (qp.hasSort()) {
            query.append(" order by ")
                    .append(qp.getSort())
                    .append((qp.isSortAsc()) ? " asc" : " desc");
        }

        return getSession().createQuery(query.toString())
                .setFirstResult(qp.getFirst())
                .setMaxResults(qp.getCount()).iterate();
    }

    public Channel get(String name) {
        String query = "from Channel m where m.channel = :channel";

        Channel m_channel = (Channel) getSession().createQuery(query)
                .setString("channel", name)
                .setMaxResults(1)
                .uniqueResult();

        if (m_channel == null) {

            return new Channel();

        }

        return m_channel;

    }

    public Channel getChannel(String name) {
        return get(name);
    }

    private boolean isChannel(String channel) {
        return get(channel).getChannel() != null;
    }

    public void saveOrUpdate(Channel channel) {

        if (isChannel(channel.getChannel())) {
            channel.setUpdated(new Date());
            Session session = getSession();
            Transaction transaction = session.beginTransaction();
            session.update(channel);
            transaction.commit();
        } else {
            channel.setUpdated(new Date());
            Session session = getSession();
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(channel);
            transaction.commit();

        }
    }

    public Channel getChannel(Channel channel) {
        String query = "from Channel m where m.channel = :channel";

        Channel m_channel = (Channel) getSession().createQuery(query)
                .setString("channel", channel.getChannel())
                .setMaxResults(1)
                .uniqueResult();

        if (m_channel == null) {

            return new Channel();

        }

        return m_channel;

    }

}