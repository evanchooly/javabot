package javabot.operations;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.SeenDao;
import org.springframework.beans.factory.annotation.Autowired;
// Author : joed
// Date  : Apr 8, 2007

public class SeenOperation extends BotOperation {
    @Autowired
    private SeenDao dao;

    public SeenOperation(Javabot javabot) {
        super(javabot);
    }

    @Override
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage().toLowerCase();
        String channel = event.getChannel();
        String sender = event.getSender();
        if (message.startsWith("seen ")) {
            String key = message.substring("seen ".length());
            if (dao.isSeen(key, channel)) {
                messages.add(new Message(channel, event,
                    sender + ", At " + DateFormat.getInstance().format(dao.getSeen(key, channel).getUpdated()) + " "
                        + key + " " + dao.getSeen(key, channel).getMessage()));
                return messages;
            }
            messages.add(new Message(channel, event, sender + ", I have no information about \"" + key + "\""));
        }
        return messages;
    }
}