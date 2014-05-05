package javabot.operations;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import com.antwerkz.maven.SPI;
import static java.time.LocalDateTime.now;
import javabot.IrcEvent;
import javabot.Message;

@SPI(BotOperation.class)
public class DaysToChristmasOperation extends BotOperation {
  @Override
  public List<Message> handleMessage(final IrcEvent event) {
    final List<Message> responses = new ArrayList<Message>();
    if ("countdown to christmas".equals(event.getMessage().toLowerCase())) {
      LocalDateTime christmas = LocalDateTime.of(now().getYear(), Month.DECEMBER, 25, 0, 0, 0);
      LocalDateTime now = now();
      Duration duration = Duration.between(now, christmas);
      responses.add(new Message(event.getChannel(), event,
          String.format("There are %s days until Christmas.", duration.toDays())));
    }
    return responses;
  }
}
