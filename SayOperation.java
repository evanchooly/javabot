import java.util.ArrayList;
import java.util.List;

import com.rickyclarkson.java.util.TypeSafeList;

public class SayOperation implements BotOperation
{
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
}
