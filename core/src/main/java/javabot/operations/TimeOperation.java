package javabot.operations;

import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;

public class TimeOperation extends BotOperation {
    public TimeOperation(final Javabot javabot) {
        super(javabot);
    }

    @Override
    public List<Message> handleMessage(final BotEvent event) {
        final String message = event.getMessage();
        List<Message> responses = new ArrayList<Message>();
        if ("time".equals(message) || "date".equals(message)) {
            responses.add(new Message(event.getChannel(), event, Calendar.getInstance().getTime().toString()));
        }
        return responses;
    }
}
