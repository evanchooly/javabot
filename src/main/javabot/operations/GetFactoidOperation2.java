package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.dao.FactoidDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetFactoidOperation2 implements BotOperation {
    private static final Log log = LogFactory.getLog(GetFactoidOperation2.class);
    private FactoidDao m_dao;

    public GetFactoidOperation2(final FactoidDao dao) {
        m_dao = dao;
    }

    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        getFactoid(event.getMessage(), event.getSender(), messages, event.getChannel(), event);
        return messages;
    }

    private void getFactoid(String message, String sender, List<Message> messages, String channel, BotEvent event) {
        log.debug(sender + " : " + message);
        if (message.endsWith(".") || message.endsWith("?") || message.endsWith("!")) {
            message = message.substring(0, message.length() - 1);
        }
        String firstWord = message.replaceAll(" .+", "");
        String dollarOne = message.replaceFirst("[^ ]+ ", "");
        String key = message;


        if (!((m_dao.getFactoid(message.toLowerCase()).getValue() == null))) {
            message = m_dao.getFactoid(message.toLowerCase()).getValue();
            message = message.replaceAll("\\$who", sender);
            message = message.replaceAll("\\$1", dollarOne);
            message = processRandomList(message);
            if (message.startsWith("<see>")) {
                message = m_dao.getFactoid(message.substring("<see>".length())).getValue();
            }
            if (message.startsWith("<reply>")) {
                messages.add(new Message(channel, message.substring("<reply>".length()), false));
            } else if (message.startsWith("<action>")) {
                messages.add(new Message(channel, message.substring("<action>".length()), true));
            } else {
                messages.add(new Message(channel, sender + ", " + key + " is " + message, false));
            }
            } else {
            List<Message> guessed = new GuessOperation2(m_dao).handleMessage(new BotEvent(event.getChannel(), event.getSender(), event.getLogin(), event.getHostname(), "guess " + message));
            Message guessedMessage = guessed.get(0);
            if (!"No appropriate factoid found.".equals(guessedMessage.getMessage())) {
                messages.addAll(guessed);
            }
        }
        if (messages.isEmpty()) {
            messages.add(new Message(channel, sender + ", I have no idea what " + message + " is.", false));
        }
    }

    String processRandomList(String message) {
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