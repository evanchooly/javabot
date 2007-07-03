package javabot.dao.impl;

import javabot.dao.AbstractDaoHibernate;
import javabot.dao.LogsDao;
import javabot.model.Logs;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class LogsDaoHibernate extends AbstractDaoHibernate<Logs> implements LogsDao {
    public LogsDaoHibernate() {
        super(Logs.class);
    }

    @SuppressWarnings({"unchecked"})
    public List<Logs> dailyLog(String channel, Date date) {
        Calendar today = Calendar.getInstance();
        today.setTime(date);
        Calendar tomorrow = new GregorianCalendar();
        tomorrow.setTime(today.getTime());
        tomorrow.add(Calendar.DATE, 1);
        return getEntityManager().createNamedQuery(LogsDao.TODAY)
                .setParameter("channel", channel)
                .setParameter("tomorrow", tomorrow.getTime())
                .setParameter("today", today.getTime())
                .getResultList();
    }

    public void logMessage(Logs.Type type, String nick, String channel, String message) {
        Logs logMessage = new Logs();
        logMessage.setType(type);
        logMessage.setNick(nick);
        logMessage.setChannel(channel.toLowerCase());
        logMessage.setMessage(message);
        logMessage.setUpdated(new Date());
        save(logMessage);
    }

    public Logs getMessage(String nick, String channel) {
        String query = "from Logs s where s.nick = :nick AND s.channel = :channel";
        return (Logs) getEntityManager().createQuery(query)
                .setParameter("nick", nick)
                .setParameter("channel", channel.toLowerCase())
                .getSingleResult();
    }

    @SuppressWarnings({"unchecked"})
    public List<String> loggedChannels() {
        return getEntityManager().createNamedQuery(LogsDao.LOGGED_CHANNELS).getResultList();
    }

}