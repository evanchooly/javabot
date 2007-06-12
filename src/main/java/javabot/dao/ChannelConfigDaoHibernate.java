package javabot.dao;

import javabot.dao.model.ChannelConfig;
import javabot.dao.model.Factoid;
import javabot.dao.util.QueryParam;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.*;

// User: joed
// Date: Apr 11, 2007
// Time: 2:41:22 PM


public class ChannelConfigDaoHibernate extends AbstractDaoHibernate<Factoid> implements ChannelConfigDao {

    public ChannelConfigDaoHibernate() {
        super(ChannelConfig.class);
    }

    @SuppressWarnings({"unchecked"})
    public List<ChannelConfig> getChannels() {
        String query = "from ChannelConfig m";

        List<ChannelConfig> m_factoids = getSession().createQuery(query).list();

        Collections.sort(m_factoids, new Comparator<ChannelConfig>() {
            public int compare(ChannelConfig factoid, ChannelConfig factoid1) {
                return factoid.getChannel().compareTo(factoid1.getChannel());
            }
        });

        return m_factoids;
    }


    public Iterator<ChannelConfig> getIterator(QueryParam qp) {
        StringBuilder query = new StringBuilder("from ChannelConfig c");

        if (qp.hasSort()) {
            query.append(" order by ")
                    .append(qp.getSort())
                    .append((qp.isSortAsc()) ? " asc" : " desc");
        }

        return getSession().createQuery(query.toString())
                .setFirstResult(qp.getFirst())
                .setMaxResults(qp.getCount()).iterate();
    }


    public ChannelConfig get(String name) {
        String query = "from ChannelConfig m where m.channel = :channel";

        ChannelConfig m_channel = (ChannelConfig) getSession().createQuery(query)
                .setString("channel", name)
                .setMaxResults(1)
                .uniqueResult();

        if (m_channel == null) {

            return new ChannelConfig();

        }

        return m_channel;

    }


    public boolean isChannel(String channel) {
        return get(channel).getChannel() != null;
    }

    public void saveOrUpdate(ChannelConfig channel) {

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

    public ChannelConfig getChannel(ChannelConfig channel) {
        String query = "from ChannelConfig m where m.channel = :channel";

        ChannelConfig m_channel = (ChannelConfig) getSession().createQuery(query)
                .setString("channel", channel.getChannel())
                .setMaxResults(1)
                .uniqueResult();

        if (m_channel == null) {

            return new ChannelConfig();

        }

        return m_channel;

    }

}