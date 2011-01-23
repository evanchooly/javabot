package javabot.operations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Message;
import javabot.dao.FactoidDao;
import javabot.dao.LogsDao;
import javabot.model.Factoid;
import javabot.model.Logs.Type;
import org.schwering.irc.lib.IrcUser;
import org.springframework.beans.factory.annotation.Autowired;

@SPI(StandardOperation.class)
public class AddFactoidOperation extends StandardOperation {
    @Autowired
    private FactoidDao factoidDao;
    @Autowired
    private LogsDao logDao;

    @Override
    public List<Message> handleMessage(final IrcEvent event) {
        String message = event.getMessage();
        final String channel = event.getChannel();
        final IrcUser sender = event.getSender();
        final List<Message> responses = new ArrayList<Message>();
        if (message.startsWith("no ") || message.startsWith("no, ")) {
            message = message.substring(2);
            if (message.startsWith(",")) {
                message = message.substring(1);
            }
            message = message.trim();
            responses.addAll(updateFactoid(event, message));
        }
        if (responses.isEmpty()) {
            return addFactoid(event, message, channel, sender);
        }
        return responses;
    }

    private List<Message> updateFactoid(final IrcEvent event, final String message) {
        final List<Message> responses = new ArrayList<Message>();
        final int is = message.indexOf(" is ");
        if (is != -1) {
            String key = message.substring(0, is);
            key = key.replaceAll("^\\s+", "");
            final Factoid factoid = factoidDao.getFactoid(key);
            if (factoid != null) {
                final String newValue = message.substring(is + 4);
                logDao.logMessage(Type.MESSAGE, event.getSender().getNick(), event.getChannel(),
                    String.format("%s changed '%s' from '%s' to '%s'", event.getSender(), key, factoid.getValue(),
                        newValue));
                factoid.setValue(newValue);
                factoid.setUpdated(new Date());
                factoid.setUserName(event.getSender().getNick());
                factoidDao.save(factoid);
                responses.add(new Message(event.getChannel(), event, "OK, " + event.getSender() + "."));
            }
        }
        return responses;
    }

    private List<Message> addFactoid(final IrcEvent event, final String message, final String channel,
        final IrcUser sender) {
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