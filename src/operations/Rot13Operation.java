package operations;

import java.util.ArrayList;
import java.util.List;

import com.rickyclarkson.java.util.TypeSafeList;


public class Rot13Operation implements BotOperation
{
	public List handleMessage(BotEvent event)
	{
		List messages=new TypeSafeList(new ArrayList(),Message.class);
		
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
}
