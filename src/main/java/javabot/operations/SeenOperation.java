package javabot.operations;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.LogsDao;
import org.springframework.beans.factory.annotation.Autowired;
// Author : joed
// Date  : Apr 8, 2007

public class SeenOperation extends BotOperation {
    @Autowired
    private LogsDao dao;

    public SeenOperation(final Javabot javabot) {
        super(javabot);
    }

    @Override
    public List<Message> handleMessage(final BotEvent event) {
        final List<Message> messages = new ArrayList<Message>();
        final String message = event.getMessage().toLowerCase();
        final String channel = event.getChannel();
        final String sender = event.getSender();
        if (message.startsWith("seen ")) {
            final String key = message.substring("seen ".length());
            if (dao.isSeen(key, channel)) {
                messages.add(new Message(channel, event,
                    String.format("%s, %s was last seen at %s with the following entry: %s", sender, key,
                        DateFormat.getInstance().format(dao.getSeen(key, channel).getUpdated()),
                        dao.getSeen(key, channel).getMessage())));
                return messages;
            }
            messages.add(new Message(channel, event, sender + ", I have no information about \"" + key + "\""));
        }
        return messages;
    }
}