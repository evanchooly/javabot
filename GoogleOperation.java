import java.util.ArrayList;
import java.util.List;

import com.rickyclarkson.java.util.TypeSafeList;

public class GoogleOperation implements BotOperation
{
	public List handleMessage(BotEvent event)
	{
		List messages=new TypeSafeList(new ArrayList(),Message.class);
		String message=event.getMessage();
		String sender=event.getSender();
		
		if (!message.startsWith("google "))
			return messages;
		
		message=message.substring("google ".length());
	
		Googler googler=new Googler();
		
		googler.search(message);
		
		messages.add
		(
			new Message
			(
				event.getChannel(),
				"I'll tell you the results in a private "+
					"message, "+sender,
				false
			)
		);
		
		int numResults=googler.getNumberOfResults();
		
		for (int a=0;a<Math.min(numResults,5);a++)
			messages.add
			(
				new Message
				(
					sender,
					googler.getNextResult().toString(),
					false
				)
			);
		
		return messages;
	}
}
