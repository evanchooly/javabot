package javabot.operations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Message;
import javabot.dao.ShunDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Causes the bot to disregard bot triggers for a few minutes. Useful to de-fang abusive users without ejecting the bot
 * from a channel entirely.
 */
@SPI(BotOperation.class)
public class ShunOperation extends BotOperation {
  private static final Logger LOG = LoggerFactory.getLogger(ShunOperation.class);

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
    LOG.debug("DateTime.now() = " + LocalDateTime.now());
    final LocalDateTime until = parts.length == 1
        ? LocalDateTime.now().plusMinutes(5)
        : LocalDateTime.now().plusSeconds(Integer.parseInt(parts[1]));
    shunDao.addShun(victim, until);
    return String.format("%s is shunned until %s.", victim,
        until.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
  }
}