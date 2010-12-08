package javabot.operations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Message;
import org.schwering.irc.lib.IRCUser;

@SPI(BotOperation.class)
public class DaysUntilOperation extends BotOperation {
    @Override
    public List<Message> handleMessage(final IrcEvent event) {
        String message = event.getMessage().toLowerCase();
        final List<Message> responses = new ArrayList<Message>();
        if (message.startsWith("days until ")) {
            final IRCUser sender = event.getSender();
            message = message.substring("days until ".length());
            final Calendar calendar = Calendar.getInstance();
            final SimpleDateFormat sdf = new SimpleDateFormat();
            Date d = null;
            final String[] formats = {
                "yyyy/MM/dd", "MMM d, ''yy", "d MMM yyyy",
                "MMM d, yyyy", "MMM d, ''yy"
            };
            int i = 0;
            while (i < formats.length && d == null) {
                sdf.applyPattern(formats[i]);
                try {
                    d = sdf.parse(message);
                    calcTime(responses, event, message, sender, calendar, d);
                } catch (ParseException e) {
                    // I think we just want to ignore this...
                }
                i++;
            }
            if (d == null) {
                responses.add(new Message(event.getChannel(), event,
                    sender + ":  you might want to consider putting the date in a proper format..."));
            }
        }
        return responses;
    }

    private void calcTime(final List<Message> responses, final IrcEvent event, final String message,
        final IRCUser sender, final Calendar calendar, final Date d) {
        calendar.setTime(d);
        final Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        long millis = calendar.getTimeInMillis() - today.getTimeInMillis();
        final double days = millis /= 86400000;
        responses.add(new Message(event.getChannel(), event,
            sender + ":  there are " + (int) days + " days until " + message + "."));
    }
}
