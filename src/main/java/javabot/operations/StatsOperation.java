package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.dao.FactoidDao;

public class StatsOperation implements BotOperation {
    private FactoidDao factoidDao;

    public StatsOperation(FactoidDao dao) {
        factoidDao = dao;
    }

    private static final long startTime = System.currentTimeMillis();

    private int numberOfMessages = 0;

    public List<Message> handleMessage(BotEvent event) {
        numberOfMessages++;
        List<Message> messages = new ArrayList<Message>();

        String message = event.getMessage();

        if (message.toLowerCase().startsWith("stats")) {
            long uptime = System.currentTimeMillis() - startTime;
            long days = uptime / 86400000;
            messages.add(new Message(event.getChannel(), "I have been up for " + days + " days, have served "
                + numberOfMessages + " messages, and have " + factoidDao.count() + " factoids.", false));
        }

        return messages;
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}