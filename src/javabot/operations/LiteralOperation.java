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
public class LiteralOperation implements BotOperation
{
	private final Database database;
	
	public LiteralOperation(Database database)
	{
		this.database=database;
	}
		
	/**
	 * @see javabot.operations.BotOperation#handleMessage(javabot.BotEvent)
	 */
	public List handleMessage(BotEvent event)
	{
		List messages=new TypeSafeList(new ArrayList(),Message.class);
		
		String message=event.getMessage().toLowerCase();
		String channel=event.getChannel();

		if (message.startsWith("literal "))
		{
			String key=message.substring("literal ".length());

			if (database.hasFactoid(key))
			{
				messages.add
				(
					new Message
					(
						channel,
						database.getFactoid(key),
						false
					)
				);
				
				return messages;
			}

			messages.add
			(
				new Message
				(
					channel,
					"I have no factoid called \""+key+"\"",
					false
				)
			);
		}

		return messages;
	}

	public List handleChannelMessage(BotEvent event)
	{
			return new TypeSafeList(new ArrayList(),Message.class);
	}
}
