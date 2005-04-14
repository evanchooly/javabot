package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Database;
import javabot.Message;

import com.rickyclarkson.java.util.TypeSafeList;

/**
 * @author ricky_clarkson
 */
public class StatsOperation implements BotOperation {
	private final Database database;
	
	public StatsOperation(final Database database)
	{
		this.database=database;
	}
	
    private static long startTime = System.currentTimeMillis();

    private static int numberOfMessages = 0;

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    public List handleMessage(BotEvent event) {
        numberOfMessages++;

        List messages = new TypeSafeList(new ArrayList(), Message.class);

        String message = event.getMessage();

        if (message.toLowerCase().startsWith("stats")) {
            long uptime = System.currentTimeMillis() - startTime;

            long days = uptime / 86400000;

            messages.add(new Message(event.getChannel(), "I have been up for "
                + days + " days, " + "have served " + numberOfMessages
                + " messages, and have " + database.getNumberOfFactoids()
                + " factoids.", false));
        }

        return messages;
    }

    public List handleChannelMessage(BotEvent event)
    {
	    	return new TypeSafeList(new ArrayList(),Message.class);
    }
}
