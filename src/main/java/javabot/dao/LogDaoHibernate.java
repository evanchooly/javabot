package javabot.dao;

import javabot.dao.model.Factoid;
import javabot.dao.model.Logs;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

// User: joed
// Date: Apr 11, 2007
// Time: 2:41:22 PM


public class LogDaoHibernate extends AbstractDaoHibernate<Factoid> implements LogDao {

    private static final Log log = LogFactory.getLog(LogDaoHibernate.class);

    public LogDaoHibernate() {
        super(Logs.class);
    }

    public Iterator<Logs> dailyLog(String channel, Integer daysBack) {

        Date updated = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String query = "from Logs s WHERE s.channel = :channel" +
                " AND to_Date(s.updated, 'YYYY-MM-DD') = :updated" +
                " ORDER BY s.updated DESC";

        return getSession().createQuery(query)
                .setString("channel", channel)
                .setString("updated", sdf.format(updated))
                .iterate();
    }

    public Iterator<Logs> dailyLog2(String channel, Integer daysBack) {

        Date updated = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String query = "from Logs s WHERE s.channel = :channel" +
                " AND to_Date(s.updated, 'YYYY-MM-DD') = :updated" +
                " ORDER BY s.updated ASC";

        return getSession().createQuery(query)
                .setString("channel", channel)
                .setString("updated", sdf.format(updated))
                .iterate();
    }


     public Integer dailyLogCount(String channel, Integer daysBack) {
        Date updated = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String query = "select count (*) from Logs s WHERE s.channel = :channel" +
                " AND to_Date(s.updated, 'YYYY-MM-DD') = :updated" +
                " ORDER BY s.updated ASC";

        return (Integer) getSession().createQuery(query)
                .setString("channel", channel)
                .setString("updated", sdf.format(updated))
                .uniqueResult();

    }

    public void logMessage(String nick, String channel, String message) {

        Logs logMessage = new Logs();

        logMessage.setNick(nick);
        logMessage.setChannel(channel);
        logMessage.setMessage(message);
        logMessage.setUpdated(new Date());

        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.save(logMessage);
        transaction.commit();

    }

    public Logs getMessage(String nick, String channel) {
        String query = "from Logs s where s.nick = :nick" + " AND s.channel = :channel";

        Logs m_user = (Logs) getSession().createQuery(query)
                .setString("nick", nick)
                .setString("channel", channel)
                .setMaxResults(1)
                .uniqueResult();

        if (m_user == null) {


            return new Logs();
        }

        return m_user;

    }

    public List<String> loggedChannels() {
        String query = "select distinct s.channel from Logs s where s.channel like '#%'";

        List<String> m_channels = getSession().createQuery(query).list();

        if (m_channels == null) {
            return new ArrayList<String>();
        }

        return m_channels;
    }

}