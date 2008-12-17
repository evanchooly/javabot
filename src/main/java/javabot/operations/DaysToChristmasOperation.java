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
public class DaysToChristmasOperation extends BotOperation {
    public DaysToChristmasOperation(Javabot javabot) {
        super(javabot);
    }

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    @Override
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        if(!"countdown to christmas".equals(event.getMessage().toLowerCase())) {
            return messages;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 25);
        Calendar today = Calendar.getInstance();
        long millis = calendar.getTimeInMillis() - today.getTimeInMillis();
        millis /= 86400000;
        messages.add(new Message(event.getChannel(), event, "There are " + millis + " days until Christmas."));
        return messages;
    }
}
