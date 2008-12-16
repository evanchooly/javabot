package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.Javabot;
import javabot.dao.FactoidDao;

/**
 * @author ricky_clarkson
 */
public class LiteralOperation extends BotOperation {
    private final FactoidDao dao;

    public LiteralOperation(Javabot bot, FactoidDao factoidDatabase) {
        super(bot);
        dao = factoidDatabase;
    }

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    @Override
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage().toLowerCase();
        String channel = event.getChannel();
        if (message.startsWith("literal ")) {
            String key = message.substring("literal ".length());
            if (dao.hasFactoid(key)) {
                messages.add(new Message(channel, event, dao.getFactoid(key).getValue()));
                return messages;
            }
            messages.add(new Message(channel, event, "I have no factoid called \"" + key + "\""));
        }
        return messages;
    }

    @Override
    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}
