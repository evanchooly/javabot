package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;

import com.rickyclarkson.java.util.TypeSafeList;

public class QuitOperation implements BotOperation
{
	public List handleMessage(BotEvent event)
	{
		List messages=new TypeSafeList(new ArrayList(),Message.class);

		String message=event.getMessage();
	        Javabot bot=event.getBot();

		if (message.toLowerCase().startsWith("quit "))
			if (message.substring("quit ".length()).equals(bot.getNickPassword()))
				System.exit(0);

                return messages;
	}
}
