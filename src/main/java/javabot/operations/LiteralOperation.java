package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.FactoidDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ricky_clarkson
 */
public class LiteralOperation extends BotOperation {
    @Autowired
    private FactoidDao dao;

    public LiteralOperation(Javabot bot) {
        super(bot);
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
