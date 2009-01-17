package javabot.dao.impl;

import javabot.dao.AbstractDaoHibernate;
import javabot.dao.LogsDao;
import javabot.model.Logs;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LogsDaoHibernate extends AbstractDaoHibernate<Logs> implements LogsDao {
    public LogsDaoHibernate() {
        super(Logs.class);
    }

    @SuppressWarnings({"unchecked"})
    public List<Logs> dailyLog(final String channel, final Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        final Date today = cal.getTime();
        cal.add(Calendar.DATE, 1);
        final Date tomorrow = cal.getTime();
        return getEntityManager().createNamedQuery(LogsDao.TODAY)
                .setParameter("channel", channel)
                .setParameter("today", today)
                .setParameter("tomorrow", tomorrow)
                .getResultList();
    }

    public void logMessage(final Logs.Type type, final String nick, final String channel, final String message) {
        final Logs logMessage = new Logs();
        logMessage.setType(type);
        logMessage.setNick(nick);
        logMessage.setChannel(channel.toLowerCase());
        logMessage.setMessage(message);
        logMessage.setUpdated(new Date());
        save(logMessage);
    }

    public Logs getMessage(final String nick, final String channel) {
        final String query = "select s from Logs s where s.nick = :nick AND s.channel = :channel";
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