package javabot.operations;

import javabot.BotEvent;
import javabot.Message;
import javabot.dao.ChangesDao;
import javabot.dao.FactoidDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ForgetFactoidOperation implements BotOperation {
    private FactoidDao f_dao;
    private ChangesDao c_dao;
    private String htmlFile;
    private Properties properties;

    public ForgetFactoidOperation(FactoidDao factoid_dao, ChangesDao change_dao, String file) {
        this.f_dao = factoid_dao;
        this.c_dao = change_dao;
        htmlFile = file;
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
            if (f_dao.hasFactoid(key)) {
                messages.add(new Message(channel, "I forgot about " + key + ", " + sender + ".", false));
                f_dao.forgetFactoid(sender, key, c_dao, htmlFile);
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
