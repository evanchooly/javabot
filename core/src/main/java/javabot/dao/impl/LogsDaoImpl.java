package javabot.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.NoResultException;

import javabot.Seen;
import javabot.dao.AbstractDaoImpl;
import javabot.dao.ConfigDao;
import javabot.dao.LogsDao;
import javabot.model.Logs;
import javabot.model.Config;
import org.springframework.beans.factory.annotation.Autowired;

public class LogsDaoImpl extends AbstractDaoImpl<Logs> implements LogsDao {
    @Autowired
    private ConfigDao dao;

    public LogsDaoImpl() {
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
        log(logMessage);
    }

    public void log(final Logs logMessage) {
        final Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.HOUR) == 0) {
            pruneHistory();
        }
        save(logMessage);
    }

    public void pruneHistory() {
        final Calendar cal = Calendar.getInstance();
        final Config config = dao.get();
        final Integer length = config.getHistoryLength();
        if (length != null && length != 0) {
            cal.clear(Calendar.MILLISECOND);
            cal.clear(Calendar.SECOND);
            cal.clear(Calendar.HOUR);
            cal.add(Calendar.MONTH, length * -1);
            getEntityManager().createQuery("delete from Logs l where l.updated < :date")
                .setParameter("date", cal.getTime())
                .executeUpdate();
        }
    }

    @Override
    public boolean isSeen(final String nick, final String channel) {
        return getSeen(nick, channel) != null;
    }

    @Override
    public Seen getSeen(final String nick, final String channel) {
        Seen seen = null;
        try {
            seen = (Seen) getEntityManager().createNamedQuery(LogsDao.SEEN)
                .setParameter("nick", nick)
                .setParameter("channel", channel)
                .setMaxResults(1)
                .getSingleResult();
        } catch (NoResultException e) {
            // hasn't been seen yet.
        }
        return seen;
    }

    @SuppressWarnings({"unchecked"})
    public List<String> loggedChannels() {
        return getEntityManager().createNamedQuery(LogsDao.LOGGED_CHANNELS).getResultList();
    }

}