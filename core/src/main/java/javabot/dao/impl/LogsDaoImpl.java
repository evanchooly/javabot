package javabot.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.NoResultException;

import javabot.Seen;
import javabot.dao.AbstractDaoImpl;
import javabot.dao.ChannelDao;
import javabot.dao.ConfigDao;
import javabot.dao.LogsDao;
import javabot.model.Channel;
import javabot.model.Config;
import javabot.model.Logs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LogsDaoImpl extends AbstractDaoImpl<Logs> implements LogsDao {
    private static final Logger log = LoggerFactory.getLogger(LogsDaoImpl.class);
    @Autowired
    private ConfigDao dao;
    @Autowired
    private ChannelDao channelDao;

    public LogsDaoImpl() {
        super(Logs.class);
    }

    @SuppressWarnings({"unchecked"})
    public List<Logs> dailyLog(final String channel, final Date date) {
        final Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
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
        final Channel chan = channel != null ? channelDao.get(channel) : null;
        final Logs logMessage = new Logs();
        logMessage.setType(type);
        logMessage.setNick(nick);
        if (channel != null) {
            logMessage.setChannel(channel.toLowerCase());
        }
        logMessage.setMessage(message);
        logMessage.setUpdated(new Date());
        save(logMessage);
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void pruneHistory() {
        final Calendar cal = Calendar.getInstance();
        final Config config = dao.get();
        final Integer duration = config.getHistoryLength();
        if (duration != null && duration != 0) {
            cal.clear(Calendar.MILLISECOND);
            cal.clear(Calendar.SECOND);
            cal.clear(Calendar.HOUR);
            cal.add(Calendar.MONTH, duration * -1);
            log.debug(
                "pruning history older than " + new SimpleDateFormat("MM-dd-yyyy hh:mm:ss").format(cal.getTime()));
            getEntityManager().createQuery("delete from Logs l where l.updated < :date")
                .setParameter("date", cal.getTime())
                .executeUpdate();
            log.debug(
                "done pruning history older than " + new SimpleDateFormat("MM-dd-yyyy hh:mm:ss").format(cal.getTime()));
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
}