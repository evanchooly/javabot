package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.dao.ChangeDao;
import javabot.dao.FactoidDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AddFactoidOperation implements BotOperation {
    private static final Log log = LogFactory.getLog(AddFactoidOperation.class);
    private FactoidDao factoidDao;
    private ChangeDao changeDao;

    public AddFactoidOperation(FactoidDao dao, ChangeDao cDao) {
        factoidDao = dao;
        changeDao = cDao;
    }

    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        String channel = event.getChannel();
        String sender = event.getSender();

        if (message.toLowerCase().contains(" is")) {

            String key = message.substring(0, message.indexOf(" is"));
            key = key.toLowerCase();

            while (key.endsWith(".") || key.endsWith("?") || key.endsWith("!")) {
                key = key.substring(0, key.length() - 1);
            }

            String value = message.substring(message.indexOf("is ") + 3, message.length());

            if (key.trim().length() == 0) {
                messages.add(new Message(channel, "Invalid factoid name", false));
                return messages;
            }
            log.debug("Value: " + value);
            log.debug("Key: " + key);
            if (value.trim().length() == 0) {
                messages.add(new Message(channel, "Invalid factoid value", false));
                return messages;
            }
            if (factoidDao.hasFactoid(key)) {
                messages.add(new Message(channel, "I already have a factoid with that name, " + sender, false));
                return messages;
            }
            messages.add(new Message(channel, "Okay, " + sender + ".", false));
            if (value.startsWith("<see>")) {
                value = value.toLowerCase();
            }
            factoidDao.addFactoid(sender, key, value);
        } else {
            return new ArrayList<Message>();

        }
        return messages;

    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }

    public ChangeDao getChangeDao() {
        return changeDao;
    }

    public void setChangeDao(ChangeDao dao) {
        changeDao = dao;
    }

    public FactoidDao getFactoidDao() {
        return factoidDao;
    }

    public void setFactoidDao(FactoidDao dao) {
        factoidDao = dao;
    }
}