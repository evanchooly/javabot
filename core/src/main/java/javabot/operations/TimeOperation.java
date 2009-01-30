package javabot.operations;

import java.util.Calendar;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;

public class TimeOperation extends BotOperation {
    public TimeOperation(final Javabot javabot) {
        super(javabot);
    }

    @Override
    public boolean handleMessage(final BotEvent event) {
        final String message = event.getMessage();
        boolean handled = false;
        if ("time".equals(message) || "date".equals(message)) {
            getBot().postMessage(new Message(event.getChannel(), event, Calendar.getInstance().getTime().toString()));
            handled = true;
        }
        return handled;
    }
}
