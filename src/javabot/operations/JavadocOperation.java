package javabot.operations;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.rickyclarkson.java.util.TypeSafeList;

public class JavadocOperation implements BotOperation
{
	JavadocParser javadocParser;

	public List handleMessage(BotEvent event)
	{
		List messages=new TypeSafeList(new ArrayList(),Message.class);
		
		String message=event.getMessage();
		Javabot bot=event.getBot();
		
		if (message.toLowerCase().startsWith("javadoc "))
		{
			if (javadocParser==null)
			{
				String javadocSources=bot.getJavadocSources();
				String javadocBaseUrl=bot.getJavadocBaseUrl();
				
				try
				{
					javadocParser=new JavadocParser
					(
						new File(javadocSources),
						javadocBaseUrl
					);
				}
				catch (IOException exception)
				{
					throw new RuntimeException(exception);
				}
			}
			String key=message.substring
				("javadoc ".length()).trim();

			String[] urls=javadocParser.javadoc(key);
	
			String sender=event.getSender();

			for (int a=0;a<urls.length;a++)
				messages.add
				(
					new Message
					(
						event.getChannel(),
						sender+", please see "+urls[a],
						false
					)
				);
			
			if (messages.size()==0)
				messages.add
				(
					new Message
					(
						event.getChannel(),
						"I don't know of any "+
							"documentation for "+
							key,
						false
					)
				);

		}
		
		return messages;
	}
}
