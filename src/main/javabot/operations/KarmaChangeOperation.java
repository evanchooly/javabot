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
public class KarmaChangeOperation implements BotOperation {
    private final Database database;

    public KarmaChangeOperation(final Database factoidDatabase) {
        database = factoidDatabase;
    }

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    public List handleMessage(BotEvent event) {
        List messages = new TypeSafeList(new ArrayList(), Message.class);
        String message = event.getMessage();
        String sender = event.getSender();
        String channel = event.getChannel();
        if(message.indexOf(" ") != -1) {
            return messages;
        }
        if(message.endsWith("++") || message.endsWith("--")) {
            String nick = message.substring(0, message.length() - 2);
            nick = nick.toLowerCase();
            if(nick.equals(sender.toLowerCase())) {
                messages.add(new Message(channel,"Changing one's own karma" +
                    " is not permitted.", false));
                return messages;
            }
            int karma;
            try {
                karma = Integer.parseInt(database.getFactoid(
                    "karma " + nick).getValue());
            } catch(Exception exception) {
                karma = 0;
            }
            if(message.endsWith("++")) {
                karma++;
            } else {
                karma--;
            }
            database.addFactoid(sender, "karma " + nick, karma + "");
            KarmaReadOperation karmaRead = new KarmaReadOperation(database);
            messages.addAll(karmaRead.handleMessage(new BotEvent(event.getChannel(),
                event.getSender(),event.getLogin(),event.getHostname(),
                "karma " + nick)));
        }
        return messages;
    }

    public List handleChannelMessage(BotEvent event) {
        return new TypeSafeList(new ArrayList(), Message.class);
    }
}