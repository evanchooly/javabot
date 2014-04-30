package javabot.operations;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.model.IrcUser;
import javabot.Message;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@SPI(BotOperation.class)
public class DaysUntilOperation extends BotOperation {
  @Override
  public List<Message> handleMessage(final IrcEvent event) {
    String message = event.getMessage().toLowerCase();
    final List<Message> responses = new ArrayList<Message>();
    if (message.startsWith("days until ")) {
      final IrcUser sender = event.getSender();
      message = message.substring("days until ".length());
      final Calendar calendar = Calendar.getInstance();
      final SimpleDateFormat sdf = new SimpleDateFormat();
      DateTime d = null;
      final DateTimeFormatter[] formats = {
          DateTimeFormat.forPattern("yyyy/MM/dd"), DateTimeFormat.forPattern("MMM d, ''yy"),
          DateTimeFormat.forPattern("d MMM yyyy"), DateTimeFormat.forPattern("MMM d, yyyy"),
          DateTimeFormat.forPattern("MMM d, ''yy")
      };
      int i = 0;
      while (i < formats.length && d == null) {
        try {
          d = formats[i].parseDateTime(message);
          calcTime(responses, event, message, sender, d);
        } catch (IllegalArgumentException e) {
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
      final IrcUser sender, final DateTime d) {
    final long days = new Duration(d, new DateTime().withTimeAtStartOfDay()).getStandardDays();
    responses.add(new Message(event.getChannel(), event,
        String.format("%s:  there are %d days until %s.", sender, days, message)));
  }
}
