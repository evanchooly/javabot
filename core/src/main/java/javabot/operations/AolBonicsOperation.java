package javabot.operations;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SPI(BotOperation.class)
public class AolBonicsOperation extends BotOperation {
    private static final Logger log = LoggerFactory.getLogger(AolBonicsOperation.class);
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
    public List<Message> handleChannelMessage(final IrcEvent event) {
        final String message = event.getMessage();
        final String channel = event.getChannel();
        final String[] split = message.split(" ");
        final List<Message> responses = new ArrayList<Message>();
        for (final String bad : split) {
            if (phrases.contains(bad.toLowerCase().replaceAll("!|\\.|\\?|,", "")) && responses.isEmpty()) {
                responses.add( new Message(channel, event,
                    String.format("%s: Please skip the aolbonics, %s", event.getSender(), getInsult())));
            }
        }
        return responses;
    }

    private String getInsult() {
        return insults.get(random.nextInt(insults.size()));
    }
}
