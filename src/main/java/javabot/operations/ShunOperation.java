package javabot.operations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.ShunDao;

/**
 * Causes the bot to disregard bot triggers for a few minutes. Useful to de-fang
 * abusive users without ejecting the bot from a channel entirely.
 */
public class ShunOperation extends BotOperation {
  private static final long MILLISECOND   = 1;
  private static final long SECOND        = 1000 * MILLISECOND;
  private static final long MINUTE        = 60 * SECOND;
  private static final long SHUN_DURATION = 5 * MINUTE;

  @Autowired
  private ShunDao           shunDao;

  public ShunOperation (Javabot javabot) {
	super(javabot);
  }

  public List<Message> handleChannelMessage (BotEvent event) {
    return Collections.emptyList ();
  }

  public List<Message> handleMessage (BotEvent event) {
    return handleMessageForDestination (event, event.getChannel ());
  }

  private Date calculateShunExpiry () {
    return new Date (System.currentTimeMillis () + SHUN_DURATION);
  }

  private List<Message> handleMessageForDestination (BotEvent event,
      String destination) {

    List<Message> messages = new ArrayList<Message> ();
    String message = event.getMessage ();
    String[] parts = message.split (" ");

    if ((parts.length == 2) && parts[0].equals ("shun")) {
      String reply = shun (parts[1]);

      messages.add (new Message (destination, event, reply));
    }
    return messages;
  }

  private String shun (String victim) {
    if (shunDao.isShunned (victim))
      return String.format ("%s is already shunned.", victim);

    Date until = calculateShunExpiry ();
    shunDao.addShun (victim, until);
    return String
        .format ("%s is shunned until %2$tY/%2$tm/%2$td %2$tH:%2$tM:%2$tS.",
            victim, until);
  }
}
