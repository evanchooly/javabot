package javabot.dao;

import javabot.dao.model.Factoid;
import javabot.dao.model.Seen;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.Properties;

// User: joed
// Date: Apr 11, 2007
// Time: 2:41:22 PM


public class SeenDaoHibernate extends AbstractDaoHibernate<Factoid> implements SeenDao {

    private Properties _properties;

    private HtmlRoutines html = new HtmlRoutines();

    private static final Log log = LogFactory.getLog(SeenDaoHibernate.class);

    public SeenDaoHibernate() {
        super(Seen.class);
    }

    public void logSeen(String nick, String channel, String message) {

         if (isSeen(nick, channel)) {

            Seen oldSeen = getSeen(nick, channel);
            oldSeen.setMessage(message);
            oldSeen.setUpdated(new Date());

            Session session = getSession();
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(oldSeen);
            transaction.commit();

        } else {

            Seen lastSeen = new Seen();

            lastSeen.setNick(nick);
            lastSeen.setChannel(channel);
            lastSeen.setMessage(message);
            lastSeen.setUpdated(new Date());

            Session session = getSession();
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(lastSeen);
            transaction.commit();

         }

    }

    public boolean isSeen(String nick, String channel) {
        return getSeen(nick, channel).getNick() != null;
    }

    public Seen getSeen(String nick, String channel) {
        String query = "from Seen s where s.nick = :nick" + " AND s.channel = :channel";

        Seen m_user = (Seen) getSession().createQuery(query)
                .setString("nick", nick)
                .setString("channel", channel)
                .setMaxResults(1)
                .uniqueResult();

        if (m_user == null) {

            return new Seen();
        }

        return m_user;

    }

}