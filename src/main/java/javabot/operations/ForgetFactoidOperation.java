package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.Javabot;
import javabot.dao.FactoidDao;

public class ForgetFactoidOperation extends BotOperation {
    private FactoidDao factoidDao;

    public ForgetFactoidOperation(Javabot javabot, FactoidDao dao) {
        super(javabot);
        factoidDao = dao;
    }

    @Override
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
                messages.add(new Message(channel, event, "I forgot about " + key + ", " + sender + "."));
                factoidDao.delete(sender, key);
            } else {
                messages.add(new Message(channel, event,
                    "I never knew about " + key + " anyway, " + sender + "."));
            }
        }
        return messages;
    }


    @Override
    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}
