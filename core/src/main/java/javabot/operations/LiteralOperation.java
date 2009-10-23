package javabot.operations;

import java.util.List;
import java.util.ArrayList;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.model.Factoid;
import javabot.dao.FactoidDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ricky_clarkson
 */
public class LiteralOperation extends BotOperation {
    @Autowired
    private FactoidDao dao;

    public LiteralOperation(final Javabot bot) {
        super(bot);
    }

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    @Override
    public List<Message> handleMessage(final BotEvent event) {
        final String message = event.getMessage().toLowerCase();
        final String channel = event.getChannel();
        final List<Message> responses = new ArrayList<Message>();
        if (message.startsWith("literal ")) {
            final String key = message.substring("literal ".length());
            final Factoid factoid = dao.getFactoid(key);
            if (factoid != null) {
                responses.add(new Message(channel, event, factoid.getValue()));
            } else {
                responses.add(new Message(channel, event, "I have no factoid called \"" + key + "\""));
            }
        }
        return responses;
    }
}
