package javabot.dao;

import java.util.Date;
import java.util.List;

import javabot.model.Logs;

public interface LogsDao {
    String TODAY = "Logs.today";
    String LOGGED_CHANNELS = "Logs.loggedChannels";

    void logMessage(Logs.Type type, String nick, String channel, String message);

    Logs getMessage(String nick, String channel);

    List<String> loggedChannels();

    List<Logs> dailyLog(String channel, Date date);
}