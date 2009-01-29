package javabot.operations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;

public class DaysUntilOperation extends BotOperation {
    public DaysUntilOperation(final Javabot javabot) {
        super(javabot);
    }

    @Override
    public boolean handleMessage(final BotEvent event) {
        String message = event.getMessage().toLowerCase();
        boolean handled = false;
        if (message.startsWith("days until ")) {
            final String sender = event.getSender();
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
                    calcTime(event, message, sender, calendar, d);
                } catch (ParseException e) {
                    // I think we just want to ignore this...
                }
                i++;
            }
            if (d == null) {
                getBot().postMessage(new Message(event.getChannel(), event,
                    sender + ":  you might want to consider putting the date in a proper format..."));
            }
            handled = true;
        }
        return handled;
    }

    private void calcTime(final BotEvent event, final String message, final String sender, final Calendar calendar,
        final Date d) {
        calendar.setTime(d);
        final Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        long millis = calendar.getTimeInMillis() - today.getTimeInMillis();
        final double days = millis /= 86400000;
        getBot().postMessage(new Message(event.getChannel(), event,
            sender + ":  there are " + (int) days + " days until " + message + "."));
    }
}
