package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.Javabot;

/**
 * @author ricky_clarkson
 */
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

    public Magic8BallOperation(Javabot javabot) {
        super(javabot);
    }

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    @Override
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage().toLowerCase();
        String channel = event.getChannel();
        if(message.startsWith("should i ") || message.startsWith("magic8")) {
            int responseNumber = (int)(Math.random() * responses.length);
            messages.add(new Message(channel, event, responses[responseNumber]));
        }
        return messages;
    }

    @Override
    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}
