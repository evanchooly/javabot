package javabot.operations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.Javabot;

/**
 * @author ricky_clarkson
 */
public class TimeOperation extends BotOperation {
    public TimeOperation(Javabot javabot) {
        super(javabot);
    }

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    @Override
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        if("time".equals(message) || "date".equals(message)) {
            messages.add(new Message(event.getChannel(), event, Calendar.getInstance().getTime().toString()));
        }
        return messages;
    }
}
