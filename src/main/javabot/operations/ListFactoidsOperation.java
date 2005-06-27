package javabot.operations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.rickyclarkson.java.util.TypeSafeList;
import javabot.BotEvent;
import javabot.Database;
import javabot.Message;

/**
 * This one must always be used last.
 *
 * Don't use because it takes hours to complete and javabot cannot multitask at the time.
 */
public class ListFactoidsOperation implements BotOperation {
    private final Database database;

    public ListFactoidsOperation(final Database factoidDatabase) {
        database = factoidDatabase;
    }

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    public List handleMessage(BotEvent event) {
        List messages = new TypeSafeList(new ArrayList(), Message.class);
        String channel = event.getChannel();
        String message = event.getMessage().toLowerCase();
        if(!"listkeys".equals(message)) {
            return messages;
        }
        if(channel.startsWith("#")) {
            messages.add(new Message(channel,
                "I will only list keys in a private " + "message", false));
            return messages;
        }
        Map<String, String> map = database.getMap();
        Iterator<String> iterator = map.keySet().iterator();
        while(iterator.hasNext()) {
            String next = iterator.next();
            messages.add(new Message(channel, next + " is " + map.get(next), false));
        }
        return messages;
    }

    public List handleChannelMessage(BotEvent event) {
        return new TypeSafeList(new ArrayList(), Message.class);
    }
}
