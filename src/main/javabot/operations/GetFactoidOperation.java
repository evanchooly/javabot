package javabot.operations;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import javabot.BotEvent;
import javabot.Database;
import javabot.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.Assert;

public class GetFactoidOperation implements BotOperation {
    private static Log log = LogFactory.getLog(GetFactoidOperation.class);
    private Database database;

    public GetFactoidOperation(final Database botDatabase) {
        database = botDatabase;
    }

    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = null;
        messages = new ArrayList<Message>();
        String channel = event.getChannel();
        String message = event.getMessage();
        String sender = event.getSender();
        getFactoid(message, sender, messages, channel, event);
        return messages;
    }

    private void getFactoid(String message, String sender, List<Message> messages, String channel,
        BotEvent event) {
        if(message.endsWith(".") || message.endsWith("?")
            || message.endsWith("!")) {
            message = message.substring(0, message.length() - 1);
        }
        String firstWord = message.replaceAll(" .+", "");
        String dollarOne = message.replaceFirst("[^ ]+ ", "");
        String key = message;
        if(!database.hasFactoid(message.toLowerCase())
            && database.hasFactoid(firstWord.toLowerCase() + " $1")) {
            message = firstWord + " $1";
        }
        if(database.hasFactoid(message.toLowerCase())) {
            message = database.getFactoid(message.toLowerCase()).getValue();
            message = message.replaceAll("\\$who", sender);
            message = message.replaceAll("\\$1", dollarOne);
            message = processRandomList(message);
            if(message.startsWith("<see>")) {
                message = database.getFactoid(
                    message.substring("<see>".length())).getValue();
            }
            if(message.startsWith("<reply>")) {
                messages.add(new Message(channel, message.substring("<reply>"
                    .length()), false));
            } else if(message.startsWith("<action>")) {
                messages.add(new Message(channel, message.substring("<action>"
                    .length()), true));
            } else {
                messages.add(new Message(channel, sender + ", " + key + " is "
                    + message, false));
            }
        } else {
            List<Message> guessed = new GuessOperation(database).handleMessage(new BotEvent(
                event.getChannel(), event.getSender(), event.getLogin(),
                event.getHostname(), "guess " + message));
            Message guessedMessage = (Message)guessed.get(0);
            if(!"No appropriate factoid found.".equals(guessedMessage.getMessage())) {
                messages.addAll(guessed);
            }
        }
        if(messages.size() == 0) {
            messages.add(new Message(channel, sender + ", I have no idea what "
                + message + " is.", false));
        }
    }

    protected String processRandomList(String message) {
        int index = -1;
        do {
            index = message.indexOf("(", index + 1);
            if(index == -1) {
                break;
            }
            int index2 = message.indexOf(")", index + 1);
            if(index2 == -1) {
                break;
            }
            String choice = message.substring(index + 1, index2);
            String[] choices = choice.split("\\|");
            if(choices.length < 2) {
                if(choices.length == 1) {
                    message = choices[0]; 
                }
                continue;
            }
            int chosen = (int)(Math.random() * choices.length);
            message = message.substring(0, index) + choices[chosen]
                + message.substring(index2 + 1);
        } while(true);
        return message;
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}