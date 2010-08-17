package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import com.antwerkz.maven.SPI;
import javabot.BotEvent;
import javabot.Message;

@SPI(BotOperation.class)
public class Magic8BallOperation extends BotOperation {
    String[] responses = {
        "Yes",
        "Definitely",
        "Absolutely",
        "I think so",
        "I guess that would be good",
        "I'm not really sure",
        "If you want",
        "Well, if you really want to",
        "Maybe",
        "Probably not",
        "Not really",
        "Sometimes",
        "Hmm, sounds bad",
        "No chance!",
        "No way!",
        "No",
        "Absolutely not!",
        "Definitely not!",
        "Don't do anything I wouldn't do",
        "Only on a Tuesday",
        "If I tell you that I'll have to shoot you",
        "I'm getting something about JFK, but I don't think it's relevant"
    };

    @Override
    public List<Message> handleMessage(final BotEvent event) {
        final String message = event.getMessage().toLowerCase();
        final String channel = event.getChannel();
        final List<Message> messages = new ArrayList<Message>();
        if (message.startsWith("should i ") || message.startsWith("magic8 ")) {
            messages.add(new Message(channel, event, responses[((int) (Math.random() * responses.length))]));
        }
        return messages;
    }
}
