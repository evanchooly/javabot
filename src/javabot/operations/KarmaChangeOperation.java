package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;

import com.rickyclarkson.java.util.TypeSafeList;

/**
 * @author ricky_clarkson
 */
public class KarmaChangeOperation implements BotOperation
{
	/**
	 * @see javabot.operations.BotOperation#handleMessage(javabot.BotEvent)
	 */
	public List handleMessage(BotEvent event)
	{
		List messages=new TypeSafeList(new ArrayList(),Message.class);
		
		String message=event.getMessage();
		String sender=event.getSender();
		String channel=event.getChannel();
		
		Javabot bot=event.getBot();
		
		if (message.indexOf(" ")!=-1)
			return messages;
		
		if (message.endsWith("++") || message.endsWith("--"))
		{
			String nick=message.substring(0,message.length()-2);
		
			nick=nick.toLowerCase();
		
			if (nick.equals(sender.toLowerCase()))
			{
				messages.add
				(
					new Message
					(
						channel,
						"Incrementing one's own karma"+
						" is not permitted.",
						false
					)
				);
				
				return messages;		
			}
			
			int karma;
			
			try
			{
				karma=Integer.parseInt
					(bot.getFactoid("karma "+nick));
			}
			catch (Exception exception)
			{
				karma=0;
			}
			
			if (message.endsWith("++"))
				karma++;
			else
				karma--;
			
			bot.addFactoid("karma "+nick,karma+"");
			
			KarmaReadOperation karmaRead=new KarmaReadOperation();
			
			messages.addAll
			(
				karmaRead.handleMessage
				(
					new BotEvent
					(
						bot,
						event.getChannel(),
						event.getSender(),
						event.getLogin(),
						event.getHostname(),
						"karma "+nick
					)
				)
			);
		}

		return messages;
	}
}
