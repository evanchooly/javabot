package javabot.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javabot.dao.model.Factoid;
import javabot.dao.model.Logs;
import org.hibernate.Session;
import org.hibernate.Transaction;

// User: joed
// Date: Apr 11, 2007
// Time: 2:41:22 PM

@SuppressWarnings({"unchecked"})
public class LogDaoHibernate extends AbstractDaoHibernate<Factoid> implements LogDao {
    public LogDaoHibernate() {
        super(Logs.class);
    }

    @SuppressWarnings({"unchecked"})
    public Iterator<Logs> dailyLog(String channel, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String query = "from Logs s WHERE s.channel = :channel" +
            " AND to_Date(s.updated, 'YYYY-MM-DD') = :updated" +
            " ORDER BY s.updated ASC";
        return getSession().createQuery(query)
            .setString("channel", channel)
            .setString("updated", sdf.format(date))
            .iterate();
    }

    public void logMessage(String nick, String channel, String message) {
        Logs logMessage = new Logs();
        logMessage.setNick(nick);
        logMessage.setChannel(channel.toLowerCase());
        logMessage.setMessage(message);
        logMessage.setUpdated(new Date());
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.save(logMessage);
        transaction.commit();

    }

    public Logs getMessage(String nick, String channel) {
        String query = "from Logs s where s.nick = :nick" + " AND s.channel = :channel";
        Logs m_user = (Logs)getSession().createQuery(query)
            .setString("nick", nick)
            .setString("channel", channel.toLowerCase())
            .setMaxResults(1)
            .uniqueResult();
        if(m_user == null) {
            return new Logs();
        }
        return m_user;

    }

    public List<String> loggedChannels() {
        String query = "select distinct s.channel from Logs s where s.channel like '#%'";
        List<String> m_channels = getSession().createQuery(query).list();
        if(m_channels == null) {
            return new ArrayList<String>();
        }
        return m_channels;
    }

}