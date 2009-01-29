package javabot.operations;

import java.util.Calendar;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;

public class DaysToChristmasOperation extends BotOperation {
    public DaysToChristmasOperation(final Javabot javabot) {
        super(javabot);
    }

    @Override
    public boolean handleMessage(final BotEvent event) {
        boolean handled = false;
        if ("countdown to christmas".equals(event.getMessage().toLowerCase())) {
            final Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, Calendar.DECEMBER);
            calendar.set(Calendar.DAY_OF_MONTH, 25);
            final Calendar today = Calendar.getInstance();
            long millis = calendar.getTimeInMillis() - today.getTimeInMillis();
            millis /= 86400000;
            getBot().postMessage(new Message(event.getChannel(), event, "There are " + millis + " days until Christmas."));
            handled = true;
        }
        return handled;
    }
}
