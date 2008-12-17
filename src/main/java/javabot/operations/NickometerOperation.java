package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.Javabot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ricky_clarkson
 * @janitor joed
 */
public class NickometerOperation extends BotOperation {
    private static final Logger log = LoggerFactory.getLogger(NickometerOperation.class);

    public NickometerOperation(Javabot javabot) {
        super(javabot);
    }

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    @Override
    public List<Message> handleMessage(BotEvent event) {

        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        String[] messageParts = message.split(" ");
        if(log.isDebugEnabled()) {
            log.debug("Nickometer :" + message);
        }

        List<String> words2 = new ArrayList<String>();

        for (String word1 : messageParts) {
            if (!("".equals(word1) || " ".equals(word1))) {
                words2.add(word1);
            }
        }

        if (!(words2.size() > 1 && "nickometer".equals(words2.get(0)))) {
            return messages;
        } else {
            String nick = words2.get(1);
            if (nick.length() == 0) {
                return messages;
            }
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
            messages.add(new Message(event.getChannel(), event, "The nick " + nick + " is " + lameness + "% lame."));
        }
        return messages;
    }
}
