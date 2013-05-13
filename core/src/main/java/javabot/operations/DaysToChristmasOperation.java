package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Message;
import org.joda.time.DateTime;
import org.joda.time.Duration;

@SPI(BotOperation.class)
public class DaysToChristmasOperation extends BotOperation {
  @Override
  public List<Message> handleMessage(final IrcEvent event) {
    final List<Message> responses = new ArrayList<Message>();
    if ("countdown to christmas".equals(event.getMessage().toLowerCase())) {
      DateTime christmas = new DateTime().withDayOfMonth(25).withMonthOfYear(12);
      DateTime now = new DateTime();
      Duration duration = new Duration(now, christmas);
      responses.add(new Message(event.getChannel(), event,
          String.format("There are %s days until Christmas.", duration.toStandardDays().getDays())));
    }
    return responses;
  }
}
