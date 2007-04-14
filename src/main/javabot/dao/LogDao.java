package javabot.dao;

import javabot.dao.model.seen;

public interface LogDao {

    void  logMessage(String nick, String channel, String message);
    seen  getMessage(String nick, String channel);
    
}