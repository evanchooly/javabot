package javabot.operations;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Javabot;
import javabot.Message;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

@SPI(BotOperation.class)
public class IgnoreOperation extends BotOperation {
    @Override
    public List<Message> handleMessage(final IrcEvent event) {
        final String message = event.getMessage();
        Javabot bot = getBot();
        final String[] parts = message.split(" ");
        final List<Message> responses = new ArrayList<Message>();
        if (message.startsWith("info ")) {
            bot.addIgnore(parts[1]);
              responses.add(new Message(event.getChannel(), event, format("I am now ignoring %s", parts[1])));
        }
        return responses;
    }
}
