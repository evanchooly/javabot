package javabot.dao;

import java.util.Date;
import java.util.List;

import javabot.model.Logs;
import javabot.Seen;

public interface LogsDao extends BaseDao<Logs> {
    String TODAY = "Logs.today";
    String LOGGED_CHANNELS = "Logs.loggedChannels";
    String SEEN = "Logs.seen";

    void logMessage(Logs.Type type, String nick, String channel, String message);

    List<String> loggedChannels();

    List<Logs> dailyLog(String channel, Date date);

    Seen getSeen(String nick, String channel);

    boolean isSeen(String nick, String channel);

    void pruneHistory();
}