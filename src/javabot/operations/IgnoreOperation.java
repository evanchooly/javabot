package javabot.operations;

import java.util.ArrayList;
import java.util.List;
import com.rickyclarkson.java.util.TypeSafeList;
import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;

public class IgnoreOperation implements BotOperation {
    public List handleMessage(BotEvent event) {
        List messages = new TypeSafeList(new ArrayList(), Message.class);
        String message = event.getMessage();
        Javabot bot = event.getBot();
        String[] parts = message.split(" ");
        if((parts.length == 3)
            && parts[0].equals("quit ")
            && parts[2].equals(bot.getNickPassword())) {

            bot.addIgnore(parts[1]);
        }
        return messages;
    }

    public List handleChannelMessage(BotEvent event)
    {
	    	return new TypeSafeList(new ArrayList(),Message.class);
    }
}
