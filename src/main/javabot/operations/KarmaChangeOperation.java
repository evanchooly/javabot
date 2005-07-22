package javabot.operations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javabot.BotEvent;
import javabot.Database;
import javabot.Factoid;
import javabot.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author ricky_clarkson
 */
public class KarmaChangeOperation implements BotOperation {
    private final Database database;
    private static Log log = LogFactory.getLog(KarmaChangeOperation.class);

    public KarmaChangeOperation(final Database factoidDatabase) {
        database = factoidDatabase;
    }

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        String sender = event.getSender();
        String channel = event.getChannel();
        if(message.indexOf(" ") != -1) {
            return messages;
        }
        if(message.endsWith("++") || message.endsWith("--")) {
            String nick = message.substring(0, message.length() - 2);
            nick = nick.toLowerCase();
            int karma = 0;
            boolean newKarma = false;
            Factoid factoid = database.getFactoid("karma " + nick);
            log.debug("factoid = " + factoid);
            try {
                karma = Integer.parseInt(factoid.getValue());
            } catch(Exception exception) {
                newKarma = true;
                factoid = new Factoid(-1, "karma " + nick, "0", sender, new Date());
            }
            if(nick.equals(sender.toLowerCase())) {
                messages.add(new Message(channel, "Changing one's own karma" +
                    " is not permitted.", false));
                message = "--";
            }
            if(message.endsWith("++")) {
                karma++;
            } else {
                karma--;
            }
            factoid.setValue("" + karma, sender);
            if(newKarma) {
                database.addFactoid(factoid.getUser(), factoid.getName(), factoid.getValue());
            } else {
                database.updateFactoid(factoid);
            }
            KarmaReadOperation karmaRead = new KarmaReadOperation(database);
            messages.addAll(karmaRead.handleMessage(new BotEvent(event.getChannel(),
                event.getSender(), event.getLogin(), event.getHostname(),
                "karma " + nick)));
        }
        return messages;
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}
