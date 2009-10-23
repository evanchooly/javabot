package javabot.operations;

import java.util.List;
import java.util.ArrayList;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;

public class SayOperation extends BotOperation {
    public SayOperation(final Javabot javabot) {
        super(javabot);
    }

    @Override
    public List<Message> handleMessage(final BotEvent event) {
        String message = event.getMessage();
        List<Message> responses = new ArrayList<Message>();
        if (message.startsWith("say ")) {
            responses.add(new Message(event.getChannel(), event, message.substring("say ".length())));
        }
        return responses;
    }
}