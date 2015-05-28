package javabot.operations;

import javabot.Message;

import java.util.Calendar;

public class TimeOperation extends BotOperation {
    @Override
    public boolean handleMessage(final Message event) {
        final String message = event.getValue();
        if ("time".equals(message) || "date".equals(message)) {
            getBot().postMessageToChannel(event, Calendar.getInstance().getTime().toString());
            return true;
        }
        return false;
    }
}
