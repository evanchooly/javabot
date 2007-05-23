package javabot.dao;

import javabot.dao.model.Logs;

import java.util.Iterator;
import java.util.List;
import java.util.Date;

public interface LogDao {

    void logMessage(String nick, String channel, String message);

    Logs getMessage(String nick, String channel);

    List<String> loggedChannels();

    Iterator<Logs> dailyLog(String channel, Date date);

}