package javabot.dao;

import javabot.dao.model.factoids;
import javabot.dao.model.seen;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;

// User: joed
// Date: Apr 11, 2007
// Time: 2:41:22 PM


public class SeenDaoHibernate extends AbstractDaoHibernate<factoids> implements SeenDao {

    private Properties _properties;

    private HtmlRoutines html = new HtmlRoutines();

    private static final Log log = LogFactory.getLog(SeenDaoHibernate.class);

    public SeenDaoHibernate() {
        super(seen.class);
    }

    public void logSeen(String nick, String channel, String message) {

        seen lastSeen = new seen();

        lastSeen.setNick(nick);
        lastSeen.setChannel(channel);
        lastSeen.setMessage(message);
        lastSeen.setUpdated(new Date());

        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(lastSeen);
        transaction.commit();

    }

    public boolean isSeen(String nick, String channel) {
        return getSeen(nick,channel).getNick() != null;
    }

    public seen getSeen(String nick, String channel) {
        String query = "from seen s where s.nick = :nick" +
                       " AND s.channel = :channel";

        seen m_user = (seen) getSession().createQuery(query)
                .setString("nick", nick)
                .setString("channel", channel)
                .setMaxResults(1)
                .uniqueResult();

        if (m_user == null) {

            seen notFound = new seen();
            return notFound;
        }

        return m_user;

    }

}