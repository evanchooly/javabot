package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.dao.ChangesDao;
import javabot.dao.FactoidDao;

public class ForgetFactoidOperation implements BotOperation {
    private FactoidDao factoidDao;
    private ChangesDao changesDao;

    public ForgetFactoidOperation(FactoidDao dao, ChangesDao cDao) {
        factoidDao = dao;
        changesDao = cDao;
    }

    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String channel = event.getChannel();
        String message = event.getMessage();
        String sender = event.getSender();

        if (!message.startsWith("forget ")) {
            return messages;
        } else {
            message = message.substring("forget ".length());
            if (message.endsWith(".") || message.endsWith("?") || message.endsWith("!")) {
                message = message.substring(0, message.length() - 1);
            }
            String key = message;
            key = key.toLowerCase();
            if (factoidDao.hasFactoid(key)) {
                messages.add(new Message(channel, "I forgot about " + key + ", " + sender + ".", false));
                factoidDao.forgetFactoid(sender, key, changesDao);
            } else {
                messages.add(new Message(channel, "I never knew about " + key + " anyway, " + sender + ".", false));
            }
        }
        return messages;
    }


    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}
