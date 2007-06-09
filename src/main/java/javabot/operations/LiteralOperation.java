package javabot.operations;

import javabot.BotEvent;
import javabot.Message;
import javabot.dao.FactoidDao;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ricky_clarkson
 */
public class LiteralOperation implements BotOperation {
    private final FactoidDao f_dao;

    public LiteralOperation(FactoidDao factoidDatabase) {
        f_dao = factoidDatabase;
    }

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage().toLowerCase();
        String channel = event.getChannel();
        if (message.startsWith("literal ")) {
            String key = message.substring("literal ".length());
            if (f_dao.hasFactoid(key)) {
                messages.add(new Message(channel, f_dao.getFactoid(key).getValue(),
                        false));
                return messages;
            }
            messages.add(new Message(channel, "I have no factoid called \"" + key + "\"",
                    false));
        }
        return messages;
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}
