package javabot.operations;

import java.util.ArrayList;
import java.util.List;

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

    public AddFactoidOperation(Javabot bot) {
        super(bot);
    }

    @Override
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        String channel = event.getChannel();
        String sender = event.getSender();
        if(message.toLowerCase().contains(" is ")) {
            String key = message.substring(0, message.indexOf(" is "));
            key = key.toLowerCase();
            while(key.endsWith(".") || key.endsWith("?") || key.endsWith("!")) {
                key = key.substring(0, key.length() - 1);
            }
            int index = message.indexOf(" is ");
            String value = null;
            if(index != -1) {
                value = message.substring(index + 4, message.length());
            }
            if(key.trim().length() == 0) {
                messages.add(new Message(channel, event, "Invalid factoid name"));
                return messages;
            }
            if(log.isDebugEnabled()) {
                log.debug("Value: " + value);
                log.debug("Key: " + key);
            }
            if(value == null || value.trim().length() == 0) {
                messages.add(new Message(channel, event, "Invalid factoid value"));
                return messages;
            }
            if(factoidDao.hasFactoid(key)) {
                messages.add(new Message(channel, event, "I already have a factoid with that name, " + sender));
                return messages;
            }
            messages.add(new Message(channel, event, "Okay, " + sender + "."));
            if(value.startsWith("<see>")) {
                value = value.toLowerCase();
            }
            factoidDao.addFactoid(sender, key, value);
        } else {
            return new ArrayList<Message>();

        }
        return messages;

    }

    @Override
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