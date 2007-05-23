package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;

import com.rickyclarkson.java.util.TypeSafeList;


/**
 * @author ricky_clarkson
 */
public class Rot13Operation implements BotOperation
{
	/**
	 * @see javabot.operations.BotOperation#handleMessage(javabot.BotEvent)
	 */
	public List<Message> handleMessage(BotEvent event)
	{
		List<Message> messages=new ArrayList<Message>();
		
		String message=event.getMessage();
		String channel=event.getChannel();
		
		if (!message.startsWith("rot13 "))
			return messages;
		
		StringBuffer answer=new StringBuffer
			(message.substring("rot13 ".length()));
		
		for (int a=0;a<answer.length();a++)
		{
			char c=answer.charAt(a);
			
			if (c>='a' && c<='z')
			{
				c+=13;
				if (c>'z')
					c-=26;
			}
			
			if (c>='A' && c<='Z')
			{
				c+=13;
				if (c>'Z')
					c-=26;
			}

			answer.setCharAt(a,c);
		}
	
		messages.add
		(
			new Message
				(channel,answer.toString(),false)
		);

		return messages;
	}

	public List<Message> handleChannelMessage(BotEvent event)
	{
			return new ArrayList<Message>();
	}
}
