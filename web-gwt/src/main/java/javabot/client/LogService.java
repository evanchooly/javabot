package javabot.client;

import java.util.List;

import javabot.model.Logs;

public interface LogService {
    List<Logs> getLogEntries(String channel);
}