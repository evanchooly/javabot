package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.Javabot;
import javabot.dao.FactoidDao;
import org.springframework.beans.factory.annotation.Autowired;

public class StatsOperation extends BotOperation {
    @Autowired
    private FactoidDao factoidDao;

    public StatsOperation(Javabot javabot) {
        super(javabot);
    }

    private static final long startTime = System.currentTimeMillis();
    private int numberOfMessages = 0;

    @Override
    public List<Message> handleMessage(BotEvent event) {
        numberOfMessages++;
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        if (message.toLowerCase().startsWith("stats")) {
            long uptime = System.currentTimeMillis() - startTime;
            long days = uptime / 86400000;
            messages.add(
                new Message(event.getChannel(), event, "I have been up for " + days + " days, have served "
                    + numberOfMessages + " messages, and have " + factoidDao.count() + " factoids."));
        }
        return messages;
    }
}