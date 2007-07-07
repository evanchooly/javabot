package javabot.dao;

import javabot.model.Seen;

public interface SeenDao extends BaseDao<Seen> {
    String BY_NAME_AND_CHANNEL = "Seen.byNameAndChannel";

    void logSeen(String nick, String channel, String message);

    Seen getSeen(String nick, String channel);

    public boolean isSeen(String nick, String channel);

}