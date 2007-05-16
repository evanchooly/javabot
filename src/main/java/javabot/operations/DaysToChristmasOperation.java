package javabot.operations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;

import com.rickyclarkson.java.util.TypeSafeList;

/**
 * @author ricky_clarkson
 */
public class DaysToChristmasOperation implements BotOperation
{
	/**
		@see BotOperation#handleMessage(BotEvent)
	*/
	public List<Message> handleMessage(BotEvent event)
	{
		List<Message> messages=new ArrayList<Message>();
		
		if (!event.getMessage().toLowerCase().equals("countdown to christmas"))
			return messages;
			
		Calendar calendar=Calendar.getInstance();
		
		calendar.set(Calendar.MONTH,Calendar.DECEMBER);
		
		calendar.set(Calendar.DAY_OF_MONTH,25);
		
		Calendar today=Calendar.getInstance();
		
		long millis=calendar.getTimeInMillis()-today.getTimeInMillis();
		
		millis/=86400000;
		
		messages.add
		(
			new Message
			(
				event.getChannel(),
				"There are "+millis+" days until Christmas.",
				false
			)
		);
		
		return messages;
	}

	public List<Message> handleChannelMessage(BotEvent event)
	{
		return new ArrayList<Message>();
	}
}
