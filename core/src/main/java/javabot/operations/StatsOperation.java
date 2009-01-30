package javabot.operations;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.FactoidDao;
import org.springframework.beans.factory.annotation.Autowired;

public class StatsOperation extends BotOperation {
    @Autowired
    private FactoidDao factoidDao;

    public StatsOperation(final Javabot javabot) {
        super(javabot);
    }

    private static final long startTime = System.currentTimeMillis();
    private int numberOfMessages = 0;

    @Override
    public boolean handleMessage(final BotEvent event) {
        numberOfMessages++;
        final String message = event.getMessage();
        boolean handled = false;
        if ("stats".equalsIgnoreCase(message)) {
            final long uptime = System.currentTimeMillis() - startTime;
            final long days = uptime / 86400000;
            getBot().postMessage(
                new Message(event.getChannel(), event, "I have been up for " + days + " days, have served "
                    + numberOfMessages + " messages, and have " + factoidDao.count() + " factoids."));
            handled = true;
        }
        return handled;
    }
}