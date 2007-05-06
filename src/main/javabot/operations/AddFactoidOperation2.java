package javabot.operations;

import javabot.BotEvent;
import javabot.Message;
import javabot.dao.ChangesDao;
import javabot.dao.FactoidDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class AddFactoidOperation2 implements BotOperation {
    private static final Log log = LogFactory.getLog(AddFactoidOperation2.class);
    private FactoidDao f_dao;
    private ChangesDao c_dao;
    private String htmlFile;

    public AddFactoidOperation2(FactoidDao f_dao, ChangesDao c_dao, String file) {
        this.f_dao = f_dao;
        this.c_dao = c_dao;
        htmlFile = file;
    }

    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        String channel = event.getChannel();
        String sender = event.getSender();

        if (message.toLowerCase().contains(" is ")) {

            String key = new String();
            String value = new String();

            key = message.substring(0, message.indexOf(" is"));
            key = key.toLowerCase();

            while (key.endsWith(".") || key.endsWith("?") || key.endsWith("!")) {
                key = key.substring(0, key.length() - 1);
            }

            value = message.substring(message.indexOf("is ") + 3, message.length());

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
            if (f_dao.hasFactoid(key)) {
                messages.add(new Message(channel, "I already have a factoid with that name, " + sender, false));
                return messages;
            }
            messages.add(new Message(channel, "Okay, " + sender + ".", false));
            if (value.startsWith("<see>")) {
                value = value.toLowerCase();
            }
            f_dao.addFactoid(sender, key, value, c_dao, htmlFile);
        }
        else {
            return new ArrayList<Message>();

        }
        return messages;

    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}