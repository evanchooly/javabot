package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.Javabot;

/**
 * @author ricky_clarkson
 */
public class Rot13Operation extends BotOperation {
    public Rot13Operation(Javabot javabot) {
        super(javabot);
    }

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    @Override
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        String channel = event.getChannel();
        if(!message.startsWith("rot13 ")) {
            return messages;
        }
        StringBuilder answer = new StringBuilder(message.substring("rot13 ".length()));
        for(int a = 0; a < answer.length(); a++) {
            char c = answer.charAt(a);
            if(c >= 'a' && c <= 'z') {
                c += 13;
                if(c > 'z') {
                    c -= 26;
                }
            }
            if(c >= 'A' && c <= 'Z') {
                c += 13;
                if(c > 'Z') {
                    c -= 26;
                }
            }
            answer.setCharAt(a, c);
        }
        messages.add(new Message(channel, event, answer.toString()));
        return messages;
    }

    @Override
    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}