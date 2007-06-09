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

    @SuppressWarnings({"unchecked"})
    public Iterator<Logs> dailyLog(String channel, Date date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String query = "from Logs s WHERE s.channel = :channel" +
                " AND DATE_FORMAT(s.updated,GET_FORMAT(DATE,'ISO')) = :updated" +
                " ORDER BY s.updated ASC";

        return getSession().createQuery(query)
                .setString("channel", channel)
                .setString("updated", sdf.format(date))
                .iterate();
    }


    public void logMessage(Logs.Type type, String nick, String channel, String message) {

        Logs logMessage = new Logs();

        logMessage.setType(type);
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

        Logs m_user = (Logs) getSession().createQuery(query)
                .setString("nick", nick)
                .setString("channel", channel.toLowerCase())
                .setMaxResults(1)
                .uniqueResult();

        if (m_user == null) {


            return new Logs();
        }

        return m_user;

    }

    @SuppressWarnings({"unchecked"})
    public List<String> loggedChannels() {
        String query = "select distinct s.channel from Logs s where s.channel like '#%'";

        List<String> m_channels = (List<String>) getSession().createQuery(query).list();

        if (m_channels == null) {
            return new ArrayList<String>();
        }

        return m_channels;
    }

}