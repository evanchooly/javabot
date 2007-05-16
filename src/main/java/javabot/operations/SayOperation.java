package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;

import com.rickyclarkson.java.util.TypeSafeList;

/**
 * @author ricky_clarkson
 */
public class SayOperation implements BotOperation
{
	/**
	 * @see javabot.operations.BotOperation#handleMessage(javabot.BotEvent)
	 */
	public List<Message> handleMessage(BotEvent event)
	{
		List<Message> messages=new ArrayList<Message>();
		
		String message=event.getMessage();
		String channel=event.getChannel();
		
		if (!message.startsWith("say "))
			return messages;
		
		message=message.substring("say ".length());
		
		messages.add
		(
			new Message
				(channel,message,false)
		);

		return messages;
	}

	public List<Message> handleChannelMessage(BotEvent event)
	{
			return new ArrayList<Message>();
	}
}
