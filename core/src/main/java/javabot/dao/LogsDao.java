package javabot.dao;

import javabot.Seen;
import javabot.model.Logs;
import javabot.model.Logs.Type;
import javabot.model.criteria.LogsCriteria;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class LogsDao extends BaseDao<Logs> {
    public static final Logger LOG = LoggerFactory.getLogger(LogsDao.class);
    @Inject
    public ConfigDao dao;
    @Inject
    public ChannelDao channelDao;

    public LogsDao() {
        super(Logs.class);
    }

    public void logMessage(final Type type, final Channel channel, final User user, final String message) {
        final Logs logMessage = new Logs();
        logMessage.setType(type);
        if (user != null) {
            logMessage.setNick(user.getNick());
        }
        if (channel != null) {
            logMessage.setChannel(channel.getName().toLowerCase());
        }
        logMessage.setMessage(message);
        logMessage.setUpdated(LocalDateTime.now());
        save(logMessage);
    }

    public boolean isSeen(final String channel, final String nick) {
        return getSeen(channel, nick) != null;
    }

    public Seen getSeen(final String channel, final String nick) {
        LogsCriteria criteria = new LogsCriteria(ds);
        criteria.upperNick().equal(nick.toUpperCase());
        criteria.channel().equal(channel);
        criteria.updated().order(false);
        Logs logs = criteria.query().get();
        return logs != null ? new Seen(logs.getChannel(), logs.getMessage(), logs.getNick(), logs.getUpdated()) : null;
    }

    private List<Logs> dailyLog(String channelName, LocalDateTime date, Boolean logged) {
        List<Logs> list = null;
        if (logged) {
            LocalDate start = date == null
                              ? LocalDate.now()
                              : date.toLocalDate();
            LocalDate tomorrow = start.plusDays(1);
            LogsCriteria criteria = new LogsCriteria(ds);
            criteria.channel(channelName);
            LocalDateTime nextMidnight = tomorrow.atStartOfDay();
            LocalDateTime lastMidnight = start.atStartOfDay();
            criteria.and(
                            criteria.updated().lessThanOrEq(nextMidnight),
                            criteria.updated().greaterThanOrEq(lastMidnight)
                        );
            list = criteria.query().asList();
        }
        return list;
    }

    public List<Logs> findByChannel(String name, LocalDateTime date, Boolean showAll) {
        javabot.model.Channel channel = channelDao.get(name);
        if (channel != null && (showAll || channel.getLogged())) {
            return dailyLog(name, date, showAll || channel.getLogged());
        } else {
            return Collections.emptyList();
        }
    }

    public void deleteAllForChannel(final String channel) {
        LogsCriteria criteria = new LogsCriteria(ds);
        criteria.channel(channel);
        ds.delete(criteria.query());
    }
}