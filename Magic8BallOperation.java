import java.util.ArrayList;
import java.util.List;

import com.rickyclarkson.java.util.TypeSafeList;

public class Magic8BallOperation implements BotOperation
{
	String[] responses=
	{
		"Yes",
		"Definitely",
		"Absolutely",
		"I think so",
		"I guess that would be good",
		"I'm not really sure",
		"If you want",
		"Well, if you really want to",
		"Maybe",
		"Probably not",
		"Not really",
		"Sometimes",
		"Hmm, sounds bad",
		"No chance!",
		"No way!",
		"No",
		"Absolutely not!",
		"Definitely not!",
		"Don't do anything I wouldn't do",
		"Only on a Tuesday",
		"If I tell you that I'll have to shoot you",
		"I'm getting something about JFK, but I don't think it's "+			"relevant"
	};
	
	public List handleMessage(BotEvent event)
	{
		List messages=new TypeSafeList(new ArrayList(),Message.class);
		
		String message=event.getMessage().toLowerCase();
		String channel=event.getChannel();

		if
		(
			message.startsWith("should i ") || 
			message.startsWith("magic8")
		)
		{
			int responseNumber=
				(int)(Math.random()*responses.length);
				
			messages.add
			(
				new Message
				(
					channel,
					responses[responseNumber],
					false
				)
			);
		}

		return messages;
	}
}