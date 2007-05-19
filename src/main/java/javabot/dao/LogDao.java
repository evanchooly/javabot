package javabot.dao;

import javabot.dao.model.Logs;

import java.util.List;

public interface LogDao {

    void logMessage(String nick, String channel, String message);

    Logs getMessage(String nick, String channel);

    List<String> loggedChannels();

}