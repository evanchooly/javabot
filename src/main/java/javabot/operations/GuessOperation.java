package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.Javabot;
import javabot.dao.FactoidDao;
import javabot.model.Factoid;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A little more intelligence creeping in. This searches the entire set of key/value pairs to find
 * out which factoid has the most matches for the inputs provided.
 */
public class GuessOperation extends BotOperation {
    private final String[] ignoreList = {"you", "and", "are", "to", "that", "your", "do", "have", "a", "the", "be", "but", "can", "i", "who", "how", "get", "by", "is", "of", "out", "me", "an", "for", "use", "he", "she", "it"};
    @Autowired
    private FactoidDao dao;

    public GuessOperation(Javabot bot) {
        super(bot);
    }

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    @Override
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage().toLowerCase();
        String channel = event.getChannel();
        if (!message.startsWith("guess ")) {
            return messages;
        }
        message = message.substring("guess ".length());
        for (String anIgnoreList : ignoreList) {
            message = message.replaceAll(" " + anIgnoreList + " ", " ");
            message = message.replaceAll("^(" + anIgnoreList + ") ", " ");
            message = message.replaceAll("^(" + anIgnoreList + ")$", "");
            message = message.replaceAll(" (" + anIgnoreList + ")$", " ");
        }
        message = message.toLowerCase();
        String[] words = message.split(" +");

        List<String> words2 = new ArrayList<String>();

        for (String word1 : words) {
            if (!("".equals(word1) || " ".equals(word1))) {
                words2.add(word1);
            }
        }

        int maxMatches = 0;
        String bestKey = "";
        for (Factoid factoid : dao.getFactoids()) {
            String nextKey = factoid.getName();
            //			String nextValue=database.getFactoid(nextKey);
            String next = nextKey;
            next = next.toLowerCase();
            if (next.indexOf("karma ") == 0) {
                continue;
            }
            if (next.contains("$1")) {
                continue;
            }
            for (String word : words2) {
                int currentMatches = 0;
                if (word.length() < 3) {
                    continue;
                }
                for (int b = 0; b < next.length(); b++) {
                    int index = next.indexOf(word);
                    if (index == -1) {
                        continue;
                    }
                    currentMatches++;
                    b += word.length();
                }
                if (currentMatches > maxMatches) {
                    bestKey = nextKey;
                    maxMatches = currentMatches;
                }
            }
        }
        if (bestKey.length() == 0) {
            messages.add(new Message(channel, event, "No appropriate factoid found."));
            return messages;
        }
        messages.add(new Message(channel, event, "I guess the factoid '" + bestKey + "' might be appropriate:"));
        messages.addAll(new GetFactoidOperation(getBot()).handleMessage(new BotEvent(event.getChannel(), event.getSender(), event.getLogin(), event
                .getHostname(), bestKey)));

        return messages;
    }
}