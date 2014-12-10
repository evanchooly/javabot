package javabot.operations;

import javabot.Message;

import java.util.Calendar;

public class TimeOperation extends BotOperation {
    @Override
    public boolean handleMessage(final Message event) {
        final String message = event.getValue();
        if ("time".equals(message) || "date".equals(message)) {
            getBot().postMessage(event.getChannel(), event.getUser(), Calendar.getInstance().getTime().toString(), event.isTell());
            return true;
        }
        return false;
    }
}
