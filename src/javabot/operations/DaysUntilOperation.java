package javabot.operations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javabot.BotEvent;
import javabot.Message;

import com.rickyclarkson.java.util.TypeSafeList;

/**
 * @author littlezoper
 */
public class DaysUntilOperation implements BotOperation
{
	/**
		@see BotOperation#handleMessage(BotEvent)
	*/
	public List handleMessage(BotEvent event)
	{
		List messages=new TypeSafeList(new ArrayList(),Message.class);
		
		String message = event.getMessage().toLowerCase();
		String sender = event.getSender();
		if (!message.startsWith("days until "))
			return messages;
			
		message = message.substring("days until ".length());

		Calendar calendar=Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat();
		Date d = null;

		String formats[] = {"yyyy/MM/dd", "MMM d, ''yy", "d MMM yyyy",
			"MMM d, yyyy", "MMM d, ''yy"};

		int i = 0;
		while ((i < formats.length) && (d == null)) {
			sdf.applyPattern(formats[i]);
			try
			{
				d = sdf.parse (message);
			} catch (ParseException e) {
				// I think we just want to ignore this...
			}

			i++;
		}


		if (d == null) {
			messages.add (new Message(event.getChannel(),
				sender + ":  you might want to consider putting the date in a proper format...",
				false));

			return messages;
		}

		calendar.setTime(d);
		Calendar today=Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
		
		long millis=calendar.getTimeInMillis()-today.getTimeInMillis();
		
		double days = millis /= 86400000;
		
		messages.add (new Message (event.getChannel(),
				sender + ":  there are "+(int)days+" days until " + message + ".",
				false));
		
		return messages;
	}
}
