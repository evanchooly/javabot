package javabot.operations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.rickyclarkson.java.util.Arrays;
import com.rickyclarkson.java.util.TypeSafeList;
import javabot.BotEvent;
import javabot.Database;
import javabot.Message;
import javabot.Factoid;

/**
 * A little more intelligence creeping in. This searches the entire set of key/value pairs to find
 * out which factoid has the most matches for the inputs provided.
 */
public class GuessOperation implements BotOperation {
    private String[] ignoreList = {
        "you",
        "and",
        "are",
        "to",
        "that",
        "your",
        "do",
        "have",
        "a",
        "the",
        "be",
        "but",
        "can",
        "i",
        "who",
        "how",
        "get",
        "by",
        "is",
        "of",
        "out",
        "me",
        "an",
        "for",
        "use",
        "he",
        "she",
        "it"
    };
    private String ignoreString = Arrays.toString(ignoreList, "|");
    private final Database database;

    public GuessOperation(final Database factoidDatabase) {
        database = factoidDatabase;
    }

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList< Message>();
        String message = event.getMessage().toLowerCase();
        String channel = event.getChannel();
        if(!message.startsWith("guess ")) {
            return messages;
        }
        message = message.substring("guess ".length());
        for(int a = 0; a < ignoreList.length; a++) {
            message = message.replaceAll(" " + ignoreList[a] + " ", " ");
            message = message.replaceAll("^(" + ignoreList[a] + ") ", " ");
            message = message.replaceAll("^(" + ignoreList[a] + ")$", "");
            message = message.replaceAll(" (" + ignoreList[a] + ")$", " ");
        }
        message = message.toLowerCase();
        String[] words = message.split(" +");
        words = (String[])Arrays.removeAll(words, " ");
        words = (String[])Arrays.removeAll(words, "");
        int maxMatches = 0;
        String bestKey = "";
        for(Factoid factoid : database.getFactoids()) {
            String nextKey = factoid.getName();
            //			String nextValue=database.getFactoid(nextKey);
            String next = nextKey;
            next = next.toLowerCase();
            if(next.indexOf("karma ") == 0) {
                continue;
            }
            for(int a = 0; a < words.length; a++) {
                int currentMatches = 0;
                if(words[a].length() < 3) {
                    continue;
                }
                for(int b = 0; b < next.length(); b++) {
                    int index = next.indexOf(words[a]);
                    if(index == -1) {
                        continue;
                    }
                    currentMatches++;
                    b += words[a].length();
                }
                if(currentMatches > maxMatches) {
                    bestKey = nextKey;
                    maxMatches = currentMatches;
                }
            }
        }
        if(bestKey.equals("")) {
            messages.add(new Message(channel, "No appropriate factoid found.",
                false));
            return messages;
        }
        messages.add(new Message(channel, "I guess the factoid '" + bestKey
            + "' might be appropriate:", false));
        messages.addAll(new GetFactoidOperation(database).handleMessage(new BotEvent(
            event.getChannel(), event.getSender(), event.getLogin(), event
            .getHostname(), bestKey)));
        return messages;
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList< Message>();
    }
}
