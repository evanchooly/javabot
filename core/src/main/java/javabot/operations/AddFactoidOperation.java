package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Message;
import javabot.dao.FactoidDao;
import org.schwering.irc.lib.IRCUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@SPI(StandardOperation.class)
public class AddFactoidOperation extends StandardOperation {
    private static final Logger log = LoggerFactory.getLogger(AddFactoidOperation.class);
    @Autowired
    private FactoidDao factoidDao;

    public AddFactoidOperation() {
    }

    @Override
    public List<Message> handleMessage(final IrcEvent event) {
        String message = event.getMessage();
        final String channel = event.getChannel();
        final IRCUser sender = event.getSender();
        if (message.startsWith("no")) {
            message = removeFactoid(event, event.getMessage());
        }
        return addFactoid(event, message, channel, sender);
    }

    private String removeFactoid(final IrcEvent event, final String message) {
        if (message.toLowerCase().startsWith("no")) {
            String actual = message.substring(2);
            if (actual.startsWith(",")) {
                actual = actual.substring(1);
            }
            actual = actual.trim();
            final int is = actual.indexOf(" is ");
            if (is != -1) {
                String key = actual.substring(0, is);
                key = key.replaceAll("^\\s+", "");
                factoidDao.delete(event.getSender().getNick(), key);
                return actual;
            }
        }
        return message;
    }

    private List<Message> addFactoid(final IrcEvent event, final String message, final String channel, final IRCUser sender) {
        final List<Message> responses = new ArrayList<Message>();
        if (message.toLowerCase().contains(" is ")) {
            String key = message.substring(0, message.indexOf(" is "));
            key = key.toLowerCase();
            while (key.endsWith(".") || key.endsWith("?") || key.endsWith("!")) {
                key = key.substring(0, key.length() - 1);
            }
            final int index = message.indexOf(" is ");
            String value = null;
            if (index != -1) {
                value = message.substring(index + 4, message.length());
            }
            if (key.trim().length() == 0) {
                responses.add(new Message(channel, event, "Invalid factoid name"));
            } else if (value == null || value.trim().length() == 0) {
                responses.add(new Message(channel, event, "Invalid factoid value"));
            } else if (factoidDao.hasFactoid(key)) {
                responses.add(
                    new Message(channel, event, String.format("I already have a factoid named %s, %s", key, sender)));
            } else {
                if (value.startsWith("<see>")) {
                    value = value.toLowerCase();
                }
                factoidDao.addFactoid(sender.getNick(), key, value);
                responses.add(new Message(channel, event, "OK, " + sender + "."));
            }
        }
        return responses;
    }
}