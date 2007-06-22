package javabot.dao;

import javabot.model.Logs;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

public interface LogDao {

    void logMessage(Logs.Type type, String nick, String channel, String message);

    Logs getMessage(String nick, String channel);

    List<String> loggedChannels();

    Iterator<Logs> dailyLog(String channel, Date date);

}