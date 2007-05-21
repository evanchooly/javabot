package javabot.dao;

import javabot.dao.model.Logs;

import java.util.Iterator;
import java.util.List;

public interface LogDao {

    void logMessage(String nick, String channel, String message);

    Logs getMessage(String nick, String channel);

    List<String> loggedChannels();

    Iterator<Logs> dailyLog(String channel, Integer daysBack);

    Integer dailyLogCount(String channel, Integer daysBack); 

}