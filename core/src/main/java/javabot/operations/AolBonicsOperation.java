package javabot.operations;

import com.antwerkz.sofia.Sofia;
import javabot.Message;

import java.util.Set;
import java.util.TreeSet;

public class AolBonicsOperation extends BotOperation {
    private final Set<String> phrases = new TreeSet<>();

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
    }

    @Override
    public boolean handleChannelMessage(final Message event) {
        for (final String bad : event.getValue().split(" ")) {
            if (phrases.contains(bad.toLowerCase().replaceAll("!|\\.|\\?|,", ""))) {
                getBot().postMessage(event.getChannel(), event.getUser(), Sofia.botAolbonics(event.getUser().getNick()),
                                     event.isTell());
                return true;
            }
        }
        return false;
    }
}
