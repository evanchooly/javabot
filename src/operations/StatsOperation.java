package operations;

import java.util.ArrayList;
import java.util.List;

import com.rickyclarkson.java.util.TypeSafeList;

public class StatsOperation implements BotOperation
{
	private static long startTime=System.currentTimeMillis();
	 
	private static int numberOfMessages=0;
	
	/**
		@see BotOperation#handleMessage(BotEvent)
	*/
	public List handleMessage(BotEvent event)
	{
		numberOfMessages++;
		
		List messages=new TypeSafeList(new ArrayList(),Message.class);
		
		String message=event.getMessage();
		
		if (message.toLowerCase().startsWith("stats"))
		{
			long uptime=System.currentTimeMillis()-startTime;
			
			uptime/=1000;
			
			long days=uptime/86400000;
			
			messages.add
			(
				new Message
				(
					event.getChannel(),
					"I have been up for "+days+" days, "+
						"have served "+
						numberOfMessages+
						" messages, and have "+
						event.getBot()
							.getNumberOfFactoids()+
						" factoids.",
					false
				)
			);
		}
		
		return messages;
	}
}
