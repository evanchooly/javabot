package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import com.rickyclarkson.java.util.Arrays;
import com.rickyclarkson.java.util.TypeSafeList;

public class NickometerOperation implements BotOperation
{
	/**
		@see BotOperation#handleMessage(BotEvent)
	*/
	public List handleMessage(BotEvent event)
	{
		List messages=new TypeSafeList(new ArrayList(),Message.class);
		
		String message=event.getMessage();
		
		Object[] messageParts=message.split(" ");
		
		messageParts=Arrays.removeAll(messageParts,"");
		
		if
		(
			!messageParts[0].equals("nickometer") ||
			messageParts.length<2
		)
			return messages;
		
		String nick=messageParts[1].toString();
	
		if (nick.length()==0)
			return messages;
	
		int lameness=0;

		if (nick.length()>0 && Character.isUpperCase(nick.charAt(0)))
			lameness--;

		for (int a=0;a<nick.length();a++)
			if (!Character.isLowerCase(nick.charAt(a)))
				lameness++;
	
		double tempLameness=(double)lameness/nick.length();

		tempLameness=Math.sqrt(tempLameness);

		lameness=(int)(tempLameness*100);
		
		messages.add
		(
			new Message
			(
				event.getChannel(),
				"The nick "+nick+" is "+lameness+"% lame.",
				false
			)
		);
		
		return messages;
	}

}
