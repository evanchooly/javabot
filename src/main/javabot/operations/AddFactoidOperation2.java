package javabot.operations;

import com.rickyclarkson.java.util.Arrays;
import javabot.BotEvent;
import javabot.Message;
import javabot.dao.ChangesDao;
import javabot.dao.FactoidDao;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AddFactoidOperation2 implements BotOperation {
    private FactoidDao f_dao;
    private ChangesDao c_dao;
    private Element root;
    private String htmlFile;
    private Properties properties;

    public AddFactoidOperation2(FactoidDao f_dao, ChangesDao c_dao, Element root) {
        this.f_dao = f_dao;
        this.c_dao = c_dao;
        this.root = root;

        htmlFile = root.getChild("factoids").getAttributeValue("htmlfilename");

    }

    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        String channel = event.getChannel();
        String sender = event.getSender();
        String[] messageParts = message.split(" ");
        int partWithIs = Arrays.search(messageParts, "is");
        if (partWithIs != -1) {
            Object keyParts = Arrays.subset(messageParts, 0, partWithIs);
            String key = Arrays.toString(keyParts, " ");
            key = key.toLowerCase();
            while (key.endsWith(".") || key.endsWith("?") || key.endsWith("!")) {
                key = key.substring(0, key.length() - 1);
            }
            String value = Arrays.toString(Arrays.subset(messageParts, partWithIs + 1, messageParts.length), " ");
            if (key.trim().length() == 0) {
                messages.add(new Message(channel, "Invalid factoid name", false));
                return messages;
            }
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
        return messages;
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}