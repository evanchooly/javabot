package javabot.dao;

import javabot.dao.model.logs;

public interface LogDao {

    void logMessage(String nick, String channel, String message);

    logs getMessage(String nick, String channel);

}