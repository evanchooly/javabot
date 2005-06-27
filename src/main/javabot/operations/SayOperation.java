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
	public List handleMessage(BotEvent event)
	{
		List messages=new TypeSafeList(new ArrayList(),Message.class);
		
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

	public List handleChannelMessage(BotEvent event)
	{
			return new TypeSafeList(new ArrayList(),Message.class);
	}
}
