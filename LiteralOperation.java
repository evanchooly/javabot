import java.util.ArrayList;
import java.util.List;

import com.rickyclarkson.java.util.TypeSafeList;

public class LiteralOperation implements BotOperation
{
	public List handleMessage(BotEvent event)
	{
		List messages=new TypeSafeList(new ArrayList(),Message.class);
		
		String message=event.getMessage().toLowerCase();
		String channel=event.getChannel();

		Javabot bot=event.getBot();

		if (message.startsWith("literal "))
		{
			String key=message.substring("literal ".length());

			if (bot.hasFactoid(key))
			{
				messages.add
				(
					new Message
					(
						channel,
						bot.getFactoid(key),
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
}