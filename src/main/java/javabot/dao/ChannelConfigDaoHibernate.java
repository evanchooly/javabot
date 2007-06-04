package javabot.dao;

import javabot.dao.model.Change;
import javabot.dao.model.Factoid;
import javabot.dao.model.ChannelConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

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


    public ChannelConfig getChannel(String channel) {
            String query = "from ChannelConfig m where m.channel = :channel";

            ChannelConfig m_channel = (ChannelConfig) getSession().createQuery(query)
                    .setString("channel", channel)
                    .setMaxResults(1)
                    .uniqueResult();

            if (m_channel == null) {

               return  new ChannelConfig();

            }

            return m_channel;

        }

}