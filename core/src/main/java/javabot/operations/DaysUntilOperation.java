package javabot.operations;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Message;
import javabot.model.IrcUser;

@SPI(BotOperation.class)
public class DaysUntilOperation extends BotOperation {
  @Override
  public List<Message> handleMessage(final IrcEvent event) {
    String message = event.getMessage().toLowerCase();
    final List<Message> responses = new ArrayList<Message>();
    if (message.startsWith("days until ")) {
      final IrcUser sender = event.getSender();
      message = message.substring("days until ".length());
      LocalDateTime d = null;
      final DateTimeFormatter[] formats = {
          DateTimeFormatter.ofPattern("yyyy/MM/dd"), DateTimeFormatter.ofPattern("MMM d, ''yy"),
          DateTimeFormatter.ofPattern("d MMM yyyy"), DateTimeFormatter.ofPattern("MMM d, yyyy"),
          DateTimeFormatter.ofPattern("MMM d, ''yy")
      };
      int i = 0;
      while (i < formats.length && d == null) {
        try {
          d = LocalDateTime.parse(message, formats[i]);
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
      final IrcUser sender, final LocalDateTime d) {
    final long days = Duration.between(d, LocalDateTime.now().withHour(0)).toDays();
    responses.add(new Message(event.getChannel(), event,
        String.format("%s:  there are %d days until %s.", sender, days, message)));
  }
}
