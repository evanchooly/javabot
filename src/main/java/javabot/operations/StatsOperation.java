package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Database;
import javabot.Message;

/**
 * @author ricky_clarkson
 */
public class StatsOperation implements BotOperation {
    private final Database database;

    public StatsOperation(final Database database) {
        this.database = database;
    }

    private static final long START_TIME = System.currentTimeMillis();
    private static int numberOfMessages = 0;

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    public List<Message> handleMessage(BotEvent event) {
        numberOfMessages++;
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        if(message.toLowerCase().startsWith("stats")) {
            long uptime = System.currentTimeMillis() - START_TIME;
            long days = uptime / 86400000;
            messages.add(new Message(event.getChannel(), "I have been up for "
                + days + " days, " + "have served " + numberOfMessages
                + " messages, and have " + database.getNumberOfFactoids()
                + " factoids.", false));
        }
        return messages;
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}
