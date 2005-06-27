package javabot.operations;

import com.rickyclarkson.java.lang.Debug;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;

import com.rickyclarkson.java.util.TypeSafeList;

public class QuitOperation implements BotOperation
{
	private final String password;

	public QuitOperation(String password)
	{
		this.password=password;
	}
	
	public List handleMessage(BotEvent event)
	{
		List messages=new TypeSafeList(new ArrayList(),Message.class);

		String message=event.getMessage();

		if (message.toLowerCase().startsWith("quit "))
			if (message.substring("quit ".length()).equals(password))
			{
				Debug.printDebug("About to quit");
				System.exit(0);
			}

                return messages;
	}

	public List handleChannelMessage(BotEvent event)
	{
			return new TypeSafeList(new ArrayList(),Message.class);
	}
}
