package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import com.rickyclarkson.java.util.TypeSafeList;
import javabot.BotEvent;
import javabot.Database;
import javabot.Message;

/**
 * @author ricky_clarkson
 */
public class LiteralOperation implements BotOperation {
    private final Database database;

    public LiteralOperation(Database factoidDatabase) {
        database = factoidDatabase;
    }

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList< Message>();
        String message = event.getMessage().toLowerCase();
        String channel = event.getChannel();
        if(message.startsWith("literal ")) {
            String key = message.substring("literal ".length());
            if(database.hasFactoid(key)) {
                messages.add(new Message(channel,database.getFactoid(key).getValue(),
                    false));
                return messages;
            }
            messages.add(new Message(channel,"I have no factoid called \"" + key + "\"",
                false));
        }
        return messages;
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList< Message>();
    }
}
