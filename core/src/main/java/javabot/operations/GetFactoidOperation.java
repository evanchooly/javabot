package javabot.operations;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javabot.Action;
import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.FactoidDao;
import javabot.model.Factoid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class GetFactoidOperation extends BotOperation {
    private static final Logger log = LoggerFactory.getLogger(GetFactoidOperation.class);
    @Autowired
    private FactoidDao factoidDao;

    public GetFactoidOperation(final Javabot bot) {
        super(bot);
    }

    @Override
    public boolean handleMessage(final BotEvent event) {
        final List<Message> messages = new ArrayList<Message>();
        getFactoid(event.getMessage(), event.getSender(), messages, event.getChannel(), event, new HashSet<String>());
        return true;
    }

    private void getFactoid(final String toFind, final String sender, final List<Message> messages,
        final String channel, final BotEvent event, final Set<String> backtrack) {
        String message = toFind;
        if (log.isDebugEnabled()) {
            log.debug(sender + " : " + message);
        }
        if (message.endsWith(".") || message.endsWith("?") || message.endsWith("!")) {
            message = message.substring(0, message.length() - 1);
        }
        final String firstWord = message.replaceAll(" .+", "");
        final String dollarOne = message.replaceFirst("[^ ]+ ", "");
        final String key = message;
        if (!factoidDao.hasFactoid(message.toLowerCase()) && factoidDao.hasFactoid(firstWord.toLowerCase() + " $1")) {
            message = firstWord + " $1";
        }
        final Factoid factoid = factoidDao.getFactoid(message.toLowerCase());
        if (factoid != null) {
            processFactoid(sender, messages, channel, event, backtrack, dollarOne, key, factoid);
        } else {
            getBot().postMessage(new Message(channel, event, sender + ", I have no idea what " + message + " is."));
        }
    }

    private void processFactoid(final String sender, final List<Message> messages, final String channel,
        final BotEvent event, final Set<String> backtrack, final String dollarOne, final String key,
        final Factoid factoid) {
        String message;
        factoid.setLastUsed(new Date());
        factoidDao.save(factoid);
        message = factoid.getValue();
        message = message.replaceAll("\\$who", sender);
        message = message.replaceAll("\\$1", dollarOne);
        message = processRandomList(message);
        if (message.startsWith("<see>")) {
            if (!backtrack.contains(message)) {
                backtrack.add(message);
                getFactoid(message.substring("<see>".length()).trim(), sender, messages, channel, event, backtrack);
            } else {
                getBot().postMessage(new Message(channel, event, "Reference loop detected for factoid '" + message + "'."));
            }
        } else if (message.startsWith("<reply>")) {
            getBot().postMessage(new Message(channel, event, message.substring("<reply>".length())));
        } else if (message.startsWith("<action>")) {
            getBot().postMessage(new Action(channel, event, message.substring("<action>".length())));
        } else {
            getBot().postMessage(new Message(channel, event, sender + ", " + key + " is " + message));
        }
    }

    protected String processRandomList(final String message) {
        String result = message;
        int index = -1;
        index = result.indexOf("(", index + 1);
        int index2 = result.indexOf(")", index + 1);
        while (index < result.length() && index != -1 && index2 != -1) {
            final String choice = result.substring(index + 1, index2);
            final String[] choices = choice.split("\\|");
            if (choices.length > 1) {
                final int chosen = (int) (Math.random() * choices.length);
                result = String .format("%s%s%s", result.substring(0, index), choices[chosen],
                    result.substring(index2 + 1));
            }
            index = result.indexOf("(", index + 1);
            index2 = result.indexOf(")", index + 1);
        }
        return result;
    }
}