package javabot.operations;

import java.util.ArrayList;
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

    public GetFactoidOperation(Javabot bot) {
        super(bot);
    }

    @Override
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
                    messages.add(new Message(channel, event,
                        "Reference loop detected for factoid '" + message + "'."));
                }
            } else if (message.startsWith("<reply>")) {
                messages.add(new Message(channel, event, message.substring("<reply>".length())));
            } else if (message.startsWith("<action>")) {
                messages.add(new Action(channel, event, message.substring("<action>".length())));
            } else {
                messages.add(new Message(channel, event, sender + ", " + key + " is " + message));
            }
        } else {
            GuessOperation operation = (GuessOperation) getBot().getOperation(GuessOperation.class.getName());
            List<Message> guessed = operation.handleMessage(new BotEvent(event.getChannel(),
                    event.getSender(), event.getLogin(), event.getHostname(), "guess " + message));
            Message guessedMessage = guessed.get(0);
            if (!"No appropriate factoid found.".equals(guessedMessage.getMessage())) {
                messages.addAll(guessed);
            }
        }
        if (messages.isEmpty()) {
            messages.add(new Message(channel, event, sender + ", I have no idea what " + message + " is."));
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
}