package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Javabot;
import javabot.Message;

/**
 * @author ricky_clarkson
 * @janitor joed
 */
@SPI(BotOperation.class)
public class NickometerOperation extends BotOperation {
    public NickometerOperation() {
    }

    public NickometerOperation(final Javabot javabot) {
        setBot(javabot);
    }

    /**
     * @see BotOperation#handleMessage(IrcEvent)
     */
    @Override
    public List<Message> handleMessage(final IrcEvent event) {
        final String message = event.getMessage();
        final String[] messageParts = message.split(" ");
        final List<String> words2 = new ArrayList<String>();
        for (final String word1 : messageParts) {
            if (!("".equals(word1) || " ".equals(word1))) {
                words2.add(word1);
            }
        }
        final List<Message> responses = new ArrayList<Message>();
        if (words2.size() > 1 && "nickometer".equals(words2.get(0))) {
            final String nick = words2.get(1);
            if (nick.length() != 0) {
                int lameness = 0;
                if (nick.length() > 0 && Character.isUpperCase(nick.charAt(0))) {
                    lameness--;
                }
                for (int a = 0; a < nick.length(); a++) {
                    if (!Character.isLowerCase(nick.charAt(a))) {
                        lameness++;
                    }
                }
                double tempLameness = (double) lameness / nick.length();
                tempLameness = Math.sqrt(tempLameness);
                lameness = (int) (tempLameness * 100);
                responses
                    .add(new Message(event.getChannel(), event, "The nick " + nick + " is " + lameness + "% lame."));
            }
        }
        return responses;
    }
}
