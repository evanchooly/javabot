package javabot.operations;

import java.util.ArrayList;
import java.util.List;
import com.rickyclarkson.java.util.Arrays;
import com.rickyclarkson.java.util.TypeSafeList;
import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;

/**
 * @author ricky_clarkson
 */
public class AddFactoidOperation implements BotOperation {
    /**
     * @param event
     *
     * @return
     */
    public List handleMessage(BotEvent event) {
        List messages = new TypeSafeList(new ArrayList(), Message.class);
        String message = event.getMessage();
        String channel = event.getChannel();
        String sender = event.getSender();
        Javabot bot = event.getBot();
        String[] messageParts = message.split(" ");
        int partWithIs = Arrays.search(messageParts, "is");
        if(partWithIs!=-1) {
		if(!bot.isValidSender(sender)) {
                Object keyParts = Arrays.subset(messageParts, 0, partWithIs);
                String key = Arrays.toString(keyParts, " ");
                key = key.toLowerCase();
                while(key.endsWith(".") || key.endsWith("?")
                    || key.endsWith("!")) {
                    key = key.substring(0, key.length() - 1);
                }

		String value=Arrays.toString
		(
			Arrays.subset
				(messageParts,partWithIs+1,messageParts.length),
			" "
		);
		
		if (key.trim().length()==0)
		{
			messages.add(new Message(channel,"Invalid key",false));
			return messages;
		}
		
		if (value.trim().length()==0)
		{
			messages.add(new Message(channel,"Invalid value",false));
			return messages;
		}
		
                if(bot.hasFactoid(key)) {
                    messages.add(new Message(channel, "I already have a factoid "
                        + "with that name, " + sender, false));
                    return messages;
                }
                messages.add(new Message(channel, "Okay, " + sender + ".", false));
                event.getBot().addFactoid(sender, key,value);
            } else {
                messages.add(new Message(channel, "Whatever, " + sender + ".",
                    false));
            }
        }
        return messages;
    }

    public List handleChannelMessage(BotEvent event)
    {
	    return new TypeSafeList(new ArrayList(),Message.class);
    }
}
