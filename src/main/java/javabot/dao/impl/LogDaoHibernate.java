package javabot.dao.impl;

import javabot.model.Factoid;
import javabot.model.Logs;
import javabot.dao.AbstractDaoHibernate;
import javabot.dao.LogDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

        Calendar today = new GregorianCalendar();
        try {
            today.setTime(sdf.parse(sdf.format(date)));
        } catch (ParseException e) {
            log.error("Could not parse dates in LogDaoHibernate....");
        }

        Calendar tomorrow = new GregorianCalendar();
        tomorrow.setTime(today.getTime());
        tomorrow.add(Calendar.DATE, 1);

        String query = "from Logs s WHERE s.channel = :channel" + " AND s.updated < :tomorrow" + " AND s.updated > :today" + " order by updated";

        return getSession().createQuery(query)
                .setString("channel", channel)
                .setString("tomorrow", sdf.format(tomorrow.getTime()))
                .setString("today", sdf.format(today.getTime()))
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