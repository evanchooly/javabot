package operations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.rickyclarkson.java.util.TypeSafeList;

public class TimeOperation implements BotOperation
{
	/**
		@see BotOperation#handleMessage(BotEvent)
	*/
	public List handleMessage(BotEvent event)
	{
		List messages=new TypeSafeList(new ArrayList(),Message.class);
		
		String message=event.getMessage();
		
		if (message.equals("time") || message.equals("date"))
		{
			messages.add
			(
				new Message
				(
					event.getChannel(),
					Calendar.getInstance().getTime().toString(),
					false
				)
			);
		}
		
		return messages;
	}

}
