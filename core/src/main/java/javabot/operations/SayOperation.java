package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Message;

@SPI(BotOperation.class)
public class SayOperation extends BotOperation {
    @Override
    public List<Message> handleMessage(final IrcEvent event) {
        final String message = event.getMessage();
        final List<Message> responses = new ArrayList<Message>();
        if (message.startsWith("say ")) {
            responses.add(new Message(event.getChannel(), event, message.substring("say ".length())));
        }
        return responses;
    }
}