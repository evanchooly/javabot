package javabot.operations;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: joed
 */
public class AolBonicsOperation extends BotOperation {
    private static final Logger log = LoggerFactory.getLogger(AolBonicsOperation.class);
    private final Set<String> phrases = new TreeSet<String>();
    private final List<String> insults = new ArrayList<String>();
    private final Random random;

    public AolBonicsOperation(Javabot bot) {
        super(bot);
        phrases.add("u");
        phrases.add("omg");
        phrases.add("plz");
        phrases.add("r");
        phrases.add("lolz");
        phrases.add("l33t");
        phrases.add("1337");
        phrases.add("kewl");
        phrases.add("ppl");
        phrases.add("ru");
        phrases.add("ur");
        phrases.add("j00");
        phrases.add("cuz");
        phrases.add("coz");
        // Slightly questionable....
        // phrases.add("lol");
        phrases.add("ftw");

        insults.add("dumbass");
        insults.add("genius");
        insults.add("Einstein");
        insults.add("pal");
        insults.add("nimrod");
        insults.add("dork");

        random = new Random();
    }

        @Override
    public List<Message> handleChannelMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        String channel = event.getChannel();
        String[] split = message.split(" ");
        if(log.isDebugEnabled()) {
            log.debug("AolBonicsOperation: " + message);
        }
        Boolean notDone = true;
        for (String bad : split) {
            if (phrases.contains(bad.toLowerCase().replaceAll("!|\\.|\\?|,", "")) && notDone) {
                notDone = false;
                messages.add(new Message(channel, event, event.getSender() + ": Please skip the aolbonics, " + getInsult()));
            }
        }
        return messages;
    }

    private String getInsult() {
        return insults.get(random.nextInt(insults.size()));
    }

}
