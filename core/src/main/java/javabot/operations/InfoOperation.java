package javabot.operations;

import java.text.SimpleDateFormat;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.model.Factoid;
import javabot.dao.FactoidDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Simple operation to pull who added the factoid and when it was added
 */
public class InfoOperation extends BotOperation {
    @Autowired
    private FactoidDao dao;

    public InfoOperation(final Javabot bot) {
        super(bot);
    }

    @Override
    public boolean handleMessage(final BotEvent event) {
        final String message = event.getMessage().toLowerCase();
        final String channel = event.getChannel();
        boolean handled = false;
        if (message.startsWith("info ")) {
            final String key = message.substring("info ".length());
            final Factoid factoid = dao.getFactoid(key);
            if (factoid != null) {
                getBot().postMessage(new Message(channel, event,
                    String.format("%s was added by: %s on %s and has a literal value of: %s", key,
                        factoid.getUserName(), formatDate(factoid), factoid.getValue())));
            }
            getBot().postMessage(new Message(channel, event, "I have no factoid called \"" + key + "\""));
            handled = true;
        }
        return handled;
    }

    private String formatDate(final Factoid factoid) {
        return new SimpleDateFormat("MM-dd-yyyy' at 'K:mm a, z").format(factoid.getUpdated());
    }
}