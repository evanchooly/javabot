package javabot.operations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Message;
import javabot.dao.ShunDao;

/**
 * Causes the bot to disregard bot triggers for a few minutes. Useful to de-fang abusive users without ejecting the bot
 * from a channel entirely.
 */
@SPI(BotOperation.class)
public class ShunOperation extends BotOperation {
  private static final long MILLISECOND = 1;
  private static final long SECOND = 1000 * MILLISECOND;
  private static final long MINUTE = 60 * SECOND;
  private static final long SHUN_DURATION = 5 * MINUTE;
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

  private Date calculateShunExpiry(final long duration) {
    return new Date(System.currentTimeMillis() + duration);
  }

  private String getShunnedMessage(final String[] parts) {
    final String victim = parts[0];
    if (shunDao.isShunned(victim)) {
      return String.format("%s is already shunned.", victim);
    }
    final Date until = calculateShunExpiry(parts.length == 1 ? SHUN_DURATION : Integer.parseInt(parts[1]) * 1000);
    shunDao.addShun(victim, until);
    return String.format("%s is shunned until %2$tY/%2$tm/%2$td %2$tH:%2$tM:%2$tS.", victim, until);
  }
}