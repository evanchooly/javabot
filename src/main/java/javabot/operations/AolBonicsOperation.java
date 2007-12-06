package javabot.operations;

import javabot.BotEvent;
import javabot.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * User: joed
 */
public class AolBonicsOperation implements BotOperation {

    private static final Log log = LogFactory.getLog(AolBonicsOperation.class);

    private final Set<String> phrases = new TreeSet<String>();
    private final List<String> insults = new ArrayList<String>();
    private final Random random;

    public AolBonicsOperation() {
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

    /**
     * @see BotOperation#handleMessage(javabot.BotEvent)
     */
    public List<Message> handleMessage(BotEvent event) {
        return new ArrayList<Message>();
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        String channel = event.getChannel();
        String[] split = message.split(" ");
        log.debug("AolBonicsOperation: " + message);
        Boolean notDone = true;
        for (String bad : split) {
            if (phrases.contains(bad.toLowerCase().replaceAll("\\!|\\.|\\?|,", "")) && notDone) {
                notDone = false;
                messages.add(new Message(channel, event.getSender()
                        + ": Please skip the aolbonics, " + getInsult(), false));
            }
        }
        return messages;
    }

    private String getInsult() {
        return insults.get(random.nextInt(insults.size()));
    }

}
