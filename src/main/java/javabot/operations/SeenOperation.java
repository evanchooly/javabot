package javabot.operations;

import java.text.DateFormat;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.LogsDao;
import org.springframework.beans.factory.annotation.Autowired;

public class SeenOperation extends BotOperation {
    @Autowired
    private LogsDao dao;

    public SeenOperation(final Javabot javabot) {
        super(javabot);
    }

    @Override
    public boolean handleMessage(final BotEvent event) {
        final String message = event.getMessage().toLowerCase();
        final String channel = event.getChannel();
        final String sender = event.getSender();
        boolean handled = false;
        if (message.startsWith("seen ")) {
            final String key = message.substring("seen ".length());
            if (dao.isSeen(key, channel)) {
                getBot().postMessage(new Message(channel, event,
                    String.format("%s, %s was last seen at %s with the following entry: %s", sender, key,
                        DateFormat.getInstance().format(dao.getSeen(key, channel).getUpdated()),
                        dao.getSeen(key, channel).getMessage())));
            }
            getBot().postMessage(new Message(channel, event,
                String.format("%s, I have no information about \"%s\"", sender, key)));
            handled = true;
        }
        return handled;
    }
}