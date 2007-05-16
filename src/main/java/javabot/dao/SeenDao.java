package javabot.dao;

import javabot.dao.model.Seen;

public interface SeenDao {

    void logSeen(String nick, String channel, String message);

    Seen getSeen(String nick, String channel);

    public boolean isSeen(String nick, String channel);

}