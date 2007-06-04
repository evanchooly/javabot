package javabot.operations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;

/**
 * @author ricky_clarkson
 */
public class TimeOperation implements BotOperation {
    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        if("time".equals(message) || "date".equals(message)) {
            messages.add(new Message(event.getChannel(), Calendar.getInstance().getTime().toString(), false));
        }
        return messages;
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}
