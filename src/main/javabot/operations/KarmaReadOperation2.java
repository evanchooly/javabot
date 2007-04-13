package javabot.operations;

import javabot.BotEvent;
import javabot.Message;
import javabot.dao.ChangesDao;
import javabot.dao.FactoidDao;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class KarmaReadOperation2 implements BotOperation {

    private FactoidDao f_dao;
    private ChangesDao c_dao;
    private Element root;
    private String htmlFile;
    private Properties properties;

    public KarmaReadOperation2(FactoidDao factoid_dao, ChangesDao change_dao, Element root) {
        this.f_dao = factoid_dao;
        this.c_dao = change_dao;
        this.root = root;

        htmlFile = root.getChild("factoids").getAttributeValue("htmlfilename");
    }

    /**
     * @see javabot.operations.BotOperation#handleMessage(javabot.BotEvent)
     */
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        String channel = event.getChannel();
        String sender = event.getSender();
        if (!message.startsWith("karma ")) {
            return messages;
        }
        String nick = message.substring("karma ".length());
        nick = nick.toLowerCase();
        if (nick.indexOf(" ") != -1) {
            messages.add(new Message(channel, "I've never seen a nick with a space " + "in, " + sender, false));
            return messages;
        }
        int karma;
        try {
            karma = Integer.parseInt(f_dao.getFactoid("karma " + nick).getValue());
        } catch (Exception exception) {
            karma = 0;
        }
        if (nick.equals(sender)) {
            messages.add(new Message(channel, sender + ", you have a karma level of " + karma + ".", false));
        } else {
            messages.add(new Message(channel, nick + " has a karma level of " + karma + ", " + sender, false));
        }
        return messages;
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}