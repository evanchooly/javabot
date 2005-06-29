package javabot.operations;

import com.rickyclarkson.java.lang.Debug;

import java.util.ArrayList;
import java.util.List;
import com.rickyclarkson.java.util.TypeSafeList;
import javabot.BotEvent;
import javabot.Message;

/**
	@deprecated weird
*/
public class IgnoreOperation implements BotOperation {
    public List<Message> handleMessage(BotEvent event) {
	    Debug.printDebug("About to exit");
	    System.exit(0);
        List<Message> messages = new ArrayList< Message>();
        String message = event.getMessage();
        //Javabot bot = event.getBot();
        String[] parts = message.split(" ");
/*        if((parts.length == 3)
            && parts[0].equals("quit ")
            && parts[2].equals(bot.getNickPassword())) {

            //bot.addIgnore(parts[1]);
        }*/
        return messages;
    }

    public List<Message> handleChannelMessage(BotEvent event)
    {
	    	return new ArrayList<Message>();
    }
}
