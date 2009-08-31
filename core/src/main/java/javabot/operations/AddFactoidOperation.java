package javabot.operations;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.ChangeDao;
import javabot.dao.FactoidDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class AddFactoidOperation extends BotOperation {
    private static final Logger log = LoggerFactory.getLogger(AddFactoidOperation.class);
    @Autowired
    private FactoidDao factoidDao;
    @Autowired
    private ChangeDao changeDao;

    public AddFactoidOperation(final Javabot bot) {
        super(bot);
    }

    @Override
    public boolean handleMessage(final BotEvent event) {
        String message = event.getMessage();
        final String channel = event.getChannel();
        final String sender = event.getSender();
        if(message.startsWith("no")) {
            message = removeFactoid(event, event.getMessage());
        }
        return addFactoid(event, message, channel, sender);
    }

    private String removeFactoid(final BotEvent event, final String message) {
        if (log.isDebugEnabled()) {
            log.debug("AddFactoidOperation: " + message);
        }
        if (message.toLowerCase().startsWith("no")) {
            String actual = message.substring(2);
            if(actual.startsWith(",")) {
                actual = actual.substring(1);
            }
            actual = actual.trim();
            if (log.isDebugEnabled()) {
                log.debug("AddFactoidOperation: " + message);
            }
            String key = actual.substring(0, actual.indexOf(" is "));
            key = key.replaceAll("^\\s+", "");
            if (log.isDebugEnabled()) {
                log.debug("AddFactoidOperation: Key " + key);
            }
            factoidDao.delete(event.getSender(), key);
            return actual;
        }
        return message;
    }

    private boolean addFactoid(final BotEvent event, final String message, final String channel, final String sender) {
        boolean handled = false;
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
            if (log.isDebugEnabled()) {
                log.debug("Value: " + value);
                log.debug("Key: " + key);
            }
            if (key.trim().length() == 0) {
                getBot().postMessage(new Message(channel, event, "Invalid factoid name"));
            } else if (value == null || value.trim().length() == 0) {
                getBot().postMessage(new Message(channel, event, "Invalid factoid value"));
            } else if (factoidDao.hasFactoid(key)) {
                getBot().postMessage(
                    new Message(channel, event, String.format("I already have a factoid named %s, %s", key, sender)));
            } else {
                if (value.startsWith("<see>")) {
                    value = value.toLowerCase();
                }
                factoidDao.addFactoid(sender, key, value);
                getBot().postMessage(new Message(channel, event, "OK, " + sender + "."));
            }
            handled = true;
        }
        return handled;
    }

    public ChangeDao getChangeDao() {
        return changeDao;
    }

    public void setChangeDao(final ChangeDao dao) {
        changeDao = dao;
    }

    public FactoidDao getFactoidDao() {
        return factoidDao;
    }

    public void setFactoidDao(final FactoidDao dao) {
        factoidDao = dao;
    }
}