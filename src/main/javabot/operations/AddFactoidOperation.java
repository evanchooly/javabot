package javabot.operations;

import java.util.ArrayList;
import java.util.List;
import com.rickyclarkson.java.util.Arrays;
import com.rickyclarkson.java.util.TypeSafeList;
import javabot.BotEvent;
import javabot.Database;
import javabot.Message;

public class AddFactoidOperation implements BotOperation {
    
	private final Database database;
	
	public AddFactoidOperation(final Database database)
	{
		this.database=database;
	}
	
	public List handleMessage(BotEvent event) {
        List messages = new TypeSafeList(new ArrayList(), Message.class);
        String message = event.getMessage();
        String channel = event.getChannel();
        String sender = event.getSender();
        String[] messageParts = message.split(" ");
        int partWithIs = Arrays.search(messageParts, "is");
        
	if(partWithIs != -1) {
            Object keyParts = Arrays.subset(messageParts, 0, partWithIs);
            String key = Arrays.toString(keyParts, " ");
            key = key.toLowerCase();
            while(key.endsWith(".") || key.endsWith("?")
                || key.endsWith("!")) {
                key = key.substring(0, key.length() - 1);
            }
            String value = Arrays.toString(Arrays.subset(messageParts,
                partWithIs + 1, messageParts.length), " ");
            if(key.trim().length() == 0) {
					 messages.add(new Message(channel, "Invalid factoid name",
						 false));
                return messages;
            }
            if(value.trim().length() == 0) {
                messages.add(new Message(channel, "Invalid factoid value",
						 false));
                return messages;
            }
            if(database.hasFactoid(key)) {
                messages.add(new Message(channel, "I already have a factoid "
                    + "with that name, " + sender, false));
                return messages;
            }
            messages.add(new Message(channel, "Okay, " + sender + ".", false));
            if(value.startsWith("<see>")) {
                value = value.toLowerCase();
            }
            database.addFactoid(sender, key, value);
        }
        return messages;
    }

    public List handleChannelMessage(BotEvent event) {
        return new TypeSafeList(new ArrayList(), Message.class);
    }
}
