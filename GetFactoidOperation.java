import java.util.ArrayList;
import java.util.List;

import com.rickyclarkson.java.util.TypeSafeList;

/**
	This one must always be used last.
*/
public class GetFactoidOperation implements BotOperation
{
	public List handleMessage(BotEvent event)
	{
		List messages=new TypeSafeList(new ArrayList(),Message.class);
		
		Javabot bot=event.getBot();
		String channel=event.getChannel();
		String message=event.getMessage();
		String sender=event.getSender();

		if
		(
			message.endsWith(".") ||
			message.endsWith("?") ||
			message.endsWith("!")
		)
			message=message.substring(0,message.length()-1);

		String firstWord=message.replaceAll(" .+","");

		String dollarOne=message.replaceFirst("[^ ]+ ","");
		
		String key=message;
		
		if
		(
			!bot.hasFactoid(message.toLowerCase()) &&
			bot.hasFactoid(firstWord.toLowerCase()+" $1")
		)
			message=firstWord+" $1";
		
		if (bot.hasFactoid(message.toLowerCase()))
		{
			message=bot.getFactoid(message.toLowerCase());

			message=message.replaceAll("\\$who",sender);
			
			message=message.replaceAll("\\$1",dollarOne);			
			
			int index=-1;
			
			do
			{
				index=message.indexOf("(",index+1);
			
				if (index==-1)
					break;
			
				int index2=message.indexOf(")",index+1);
				
				if (index2==-1)
					break;
				
				String choice=
					message.substring(index+1,index2);
				
				String[] choices=choice.split("\\|");
				
				if (choices.length<2)
					continue;
				
				int chosen=(int)(Math.random()*choices.length);
					
				message=
					message.substring(0,index)+
					choices[chosen]+
					message.substring(index2+1);
			}
			while (true);
			
			if (message.startsWith("<see>"))
				message=bot.getFactoid
					(message.substring("<see>".length()));

			if (message.startsWith("<reply>"))
			{
				messages.add
				(
					new Message
					(
						channel,
						message.substring
							("<reply>".length()),
						false
					)
				);
				
				return messages;
			}

			if (message.startsWith("<action>"))
			{
				messages.add
				(
					new Message
					(
						channel,
						message.substring
							("<action>".length()),
						true
					)
				);

				return messages;
			}
								
			messages.add
			(
				new Message
				(
					channel,
					sender+", "+key+" is "+message,
					false
				)
			);

			return messages;
		}

		List guessed=new GuessOperation().handleMessage
		(
			new BotEvent
			(
				event.getBot(),
				event.getChannel(),
				event.getSender(),
				event.getLogin(),
				event.getHostname(),
				"guess "+message
			)
		);
		
		Message guessedMessage=(Message)guessed.get(0);
		
		if
		(
			!guessedMessage.getMessage().equals
				("No appropriate factoid found.")
		)
		{
			messages.addAll(guessed);
			
			return messages;
		}
		
		messages.add
		(
			new Message
			(
				channel,
				sender+", I have no idea what "+message+
					" is.",
				false
			)
		);
			
		return messages;
	}
}
