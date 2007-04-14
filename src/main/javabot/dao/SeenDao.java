package javabot.dao;

import javabot.dao.model.seen;

public interface SeenDao {

    void  logSeen(String nick, String channel, String message);
    seen  getSeen(String nick, String channel);
    public boolean isSeen(String nick, String channel);

}