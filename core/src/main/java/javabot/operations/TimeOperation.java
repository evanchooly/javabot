package javabot.operations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Message;

@SPI(BotOperation.class)
public class TimeOperation extends BotOperation {
    @Override
    public List<Message> handleMessage(final IrcEvent event) {
        final String message = event.getMessage();
        final List<Message> responses = new ArrayList<Message>();
        if ("time".equals(message) || "date".equals(message)) {
            responses.add(new Message(event.getChannel(), event, Calendar.getInstance().getTime().toString()));
        }
        return responses;
    }
}
