package javabot.operations;

import javabot.BotEvent;
import javabot.Message;
import javabot.dao.SeenDao;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Author : joed
// Date  : Apr 8, 2007

public class SeenOperation implements BotOperation {
    private static final Logger log = LoggerFactory.getLogger(SeenOperation.class);
    private SeenDao s_dao;

    public SeenOperation(SeenDao dao) {
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