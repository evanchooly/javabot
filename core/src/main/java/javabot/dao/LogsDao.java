package javabot.dao;

import java.util.Date;
import java.util.List;

import javabot.model.Logs;
import javabot.Seen;
import org.springframework.transaction.annotation.Transactional;

public interface LogsDao extends BaseDao {
    String TODAY = "Logs.today";
    String COUNT_LOGGED = "Logs.countLogged";
    String SEEN = "Logs.seen";

    void logMessage(Logs.Type type, String nick, String channel, String message);

    List<Logs> dailyLog(String channel, Date date);

    Seen getSeen(String nick, String channel);

    boolean isSeen(String nick, String channel);

    @Transactional
    void pruneHistory();
}