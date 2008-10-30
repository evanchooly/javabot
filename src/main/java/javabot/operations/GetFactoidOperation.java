package javabot.operations;

import javabot.BotEvent;
import javabot.Message;
import javabot.dao.FactoidDao;
import javabot.model.Factoid;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetFactoidOperation implements BotOperation {
    private static final Logger log = LoggerFactory.getLogger(GetFactoidOperation.class);
    
    private FactoidDao factoidDao;

    public GetFactoidOperation(final FactoidDao dao) {
        factoidDao = dao;
    }

    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        getFactoid(event.getMessage(), event.getSender(), messages, event.getChannel(), event, new HashSet<String>());
        return messages;
    }

    private void getFactoid(String toFind, String sender, List<Message> messages, String channel, BotEvent event,
                            Set<String> backtrack) {
        String message = toFind;
        if(log.isDebugEnabled()) {
            log.debug(sender + " : " + message);
        }
        if (message.endsWith(".") || message.endsWith("?") || message.endsWith("!")) {
            message = message.substring(0, message.length() - 1);
        }
        String firstWord = message.replaceAll(" .+", "");
        String dollarOne = message.replaceFirst("[^ ]+ ", "");
        String key = message;
        if (!factoidDao.hasFactoid(message.toLowerCase()) && factoidDao.hasFactoid(firstWord.toLowerCase() + " $1")) {
            message = firstWord + " $1";
        }
        Factoid factoid = factoidDao.getFactoid(message.toLowerCase());
        if (factoid != null) {
            message = factoid.getValue();
            message = message.replaceAll("\\$who", sender);
            message = message.replaceAll("\\$1", dollarOne);
            message = processRandomList(message);
            if (message.startsWith("<see>")) {
                if (!backtrack.contains(message)) {
                    backtrack.add(message);
                    getFactoid(message.substring("<see>".length()).trim(), sender, messages, channel, event, backtrack);
                } else {
                    messages.add(new Message(channel, "Reference loop detected for factoid '" + message + "'.", false));
                }
            } else if (message.startsWith("<reply>")) {
                messages.add(new Message(channel, message.substring("<reply>".length()), false));
            } else if (message.startsWith("<action>")) {
                messages.add(new Message(channel, message.substring("<action>".length()), true));
            } else {
                messages.add(new Message(channel, sender + ", " + key + " is " + message, false));
            }
        } else {
            List<Message> guessed = new GuessOperation(factoidDao).handleMessage(new BotEvent(event.getChannel(),
                    event.getSender(), event.getLogin(), event.getHostname(), "guess " + message));
            Message guessedMessage = guessed.get(0);
            if (!"No appropriate factoid found.".equals(guessedMessage.getMessage())) {
                messages.addAll(guessed);
            }
        }
        if (messages.isEmpty()) {
            messages.add(new Message(channel, sender + ", I have no idea what " + message + " is.", false));
        }
    }

    protected String processRandomList(String message) {
        String result = message;
        int index = -1;
        index = result.indexOf("(", index + 1);
        int index2 = result.indexOf(")", index + 1);
        while (index < result.length() && index != -1 && index2 != -1) {
            String choice = result.substring(index + 1, index2);
            String[] choices = choice.split("\\|");
            if (choices.length > 1) {
                int chosen = (int) (Math.random() * choices.length);
                result = result.substring(0, index) + choices[chosen] + result.substring(index2 + 1);
            }
            index = result.indexOf("(", index + 1);
            index2 = result.indexOf(")", index + 1);
        }
        return result;
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}