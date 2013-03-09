package javabot.operations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Message;
import javabot.dao.ShunDao;
import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * Causes the bot to disregard bot triggers for a few minutes. Useful to de-fang abusive users without ejecting the bot
 * from a channel entirely.
 */
@SPI(BotOperation.class)
public class ShunOperation extends BotOperation {
  private static final long MILLISECOND = 1;
  private static final long SECOND = 1000 * MILLISECOND;
  private static final long MINUTE = 60 * SECOND;
  private static final Duration SHUN_DURATION = Duration.standardMinutes(5);
  @Inject
  private ShunDao shunDao;

  public List<Message> handleMessage(final IrcEvent event) {
    final String message = event.getMessage();
    final List<Message> responses = new ArrayList<Message>();
    if (message.startsWith("shun ")) {
      final String[] parts = message.substring(5).split(" ");
      if (parts.length == 0) {
        responses.add(new Message(event.getChannel(), event, "Usage:  shun <user> [<seconds>]"));
      } else {
        responses.add(new Message(event.getChannel(), event, getShunnedMessage(parts)));
      }
    }
    return responses;
  }

  private String getShunnedMessage(final String[] parts) {
    final String victim = parts[0];
    if (shunDao.isShunned(victim)) {
      return String.format("%s is already shunned.", victim);
    }
    System.out.println("DateTime.now() = " + DateTime.now());
    final Date until = (parts.length == 1
        ? DateTime.now().plusMinutes(5)
        : DateTime.now().plusSeconds(Integer.parseInt(parts[1])))
        .toDate();
    shunDao.addShun(victim, until);
    return String.format("%s is shunned until %2$tY/%2$tm/%2$td %2$tH:%2$tM:%2$tS.", victim, until);
  }
}