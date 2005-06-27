package javabot.operations;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import com.rickyclarkson.java.util.Arrays;
import com.rickyclarkson.java.util.TypeSafeList;
import javabot.BotEvent;
import javabot.Database;
import javabot.Message;

public class ForgetFactoidOperation implements BotOperation {
   
	private final Database database;

	public ForgetFactoidOperation(final Database factoidDatabase)
	{
        database =factoidDatabase;
	}
	
	public List handleMessage(BotEvent event) {
        List messages = new TypeSafeList(new ArrayList(), Message.class);
        String channel = event.getChannel();
        String message = event.getMessage();
        String sender = event.getSender();
        String[] messageParts = message.split(" ");
        if("forget".equals(messageParts[0])) {
            int length = Array.getLength(messageParts);
            Object keyParts = Arrays.subset(messageParts, 1, length);
            String key = Arrays.toString(keyParts, " ");
            key = key.toLowerCase();
            if(database.hasFactoid(key)) {
                messages.add(new Message(channel, "I forgot about " + key
                    + ", " + sender + ".", false));
                database.forgetFactoid(sender, key);
            } else {
                messages.add(new Message(channel, "I never knew about "
                    + key + " anyway, " + sender + ".", false));
            }
        }
        return messages;
    }

    public List handleChannelMessage(BotEvent event) {
        return new TypeSafeList(new ArrayList(), Message.class);
    }
}
