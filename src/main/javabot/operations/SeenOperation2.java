package javabot.operations;

import javabot.BotEvent;
import javabot.Message;
import javabot.dao.SeenDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

// Author : joed
// Date  : Apr 8, 2007

public class SeenOperation2 implements BotOperation {

    private static Log log = LogFactory.getLog(SeenOperation2.class);
    private SeenDao s_dao;

    public SeenOperation2(SeenDao dao) {
        this.s_dao = dao;
    }

    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage().toLowerCase();
        String channel = event.getChannel();
        String sender = event.getSender();
        if (message.startsWith("seen ")) {
            String key = message.substring("seen ".length());
            if (s_dao.isSeen(key, channel)) {
                messages.add(new Message(channel, sender + ", At " + DateFormat.getInstance().format(s_dao.getSeen(key, channel).getUpdated()) + " " + key + " " + s_dao.getSeen(key, channel).getMessage(), false));
                return messages;
            }
            messages.add(new Message(channel, sender + ", I have no information about \"" + key + "\"", false));
        }
        return messages;
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}