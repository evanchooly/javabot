package javabot.operations;

import com.rickyclarkson.java.util.Arrays;
import javabot.BotEvent;
import javabot.Message;
import javabot.dao.ChangesDao;
import javabot.dao.FactoidDao;
import org.jdom.Element;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ForgetFactoidOperation2 implements BotOperation {
    private FactoidDao f_dao;
    private ChangesDao c_dao;
    private String htmlFile;
    private Properties properties;

    public ForgetFactoidOperation2(FactoidDao factoid_dao, ChangesDao change_dao, String file) {
        this.f_dao = factoid_dao;
        this.c_dao = change_dao;

        htmlFile = file;
    }

    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String channel = event.getChannel();
        String message = event.getMessage();
        String sender = event.getSender();
        String[] messageParts = message.split(" ");
        if ("forget".equals(messageParts[0])) {
            int length = Array.getLength(messageParts);
            Object keyParts = Arrays.subset(messageParts, 1, length);
            String key = Arrays.toString(keyParts, " ");
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