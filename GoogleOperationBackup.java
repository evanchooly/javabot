import java.util.ArrayList;
import java.util.List;

import com.rickyclarkson.java.util.TypeSafeList;

public class GoogleOperationBackup implements BotOperation
{
	public List handleMessage(BotEvent event)
	{
		List messages=new TypeSafeList(new ArrayList(),Message.class);
		String message=event.getMessage();
		String channel=event.getChannel();
		
		if (!message.startsWith("google "))
			return messages;
		
		message=message.substring("google ".length());
		
		message=message.replace(' ','+');
		
		messages.add
		(
			new Message
			(
				channel,
				"http://www.google.com/search?q="+message,
				false
			)
		);
		
		return messages;
	}
}
