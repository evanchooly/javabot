package javabot.operations;

import javabot.BotEvent;
import javabot.Message;
import javabot.dao.FactoidDao;

import java.util.ArrayList;
import java.util.List;

// joed - moved to Dao's

public class StatsOperation2 implements BotOperation {
    private FactoidDao m_dao;

    public StatsOperation2(FactoidDao m_dao) {
        this.m_dao = m_dao;
    }

    private static long startTime = System.currentTimeMillis();

    private static int numberOfMessages = 0;

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    public List<Message> handleMessage(BotEvent event) {
        numberOfMessages++;
        List<Message> messages = new ArrayList<Message>();

        String message = event.getMessage();

        if (message.toLowerCase().startsWith("stats")) {
            long uptime = System.currentTimeMillis() - startTime;
            long days = uptime / 86400000;
            messages.add(new Message(event.getChannel(), "I have been up for " + days + " days, " + "have served " + numberOfMessages + " messages, and have " + m_dao.getNumberOfFactoids() + " factoids.", false));
        }

        return messages;
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}
