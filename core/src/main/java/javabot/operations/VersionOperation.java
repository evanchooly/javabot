package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Message;

@SPI(StandardOperation.class)
public class VersionOperation extends StandardOperation {
    @Override
    public List<Message> handleMessage(final IrcEvent event) {
        final String message = event.getMessage();
        final List<Message> responses = new ArrayList<Message>();
        if ("version".equalsIgnoreCase(message)) {
            responses.add(new Message(event.getChannel(), event, getBot().loadVersion()));
        }
        return responses;
    }
}