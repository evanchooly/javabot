package javabot.operations;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Message;
import javabot.model.IrcUser;
import javabot.model.Logs;
import javabot.model.criteria.LogsCriteria;
import org.mongodb.morphia.Datastore;

/**
 * Gets logs for channel, optionally filtered by a nickname
 *
 * @author StanAccy
 */
@SPI(BotOperation.class)
public class LogsOperation extends BotOperation {
  private static final String KEYWORD_LOGS = "logs";

  @Inject
  private Datastore ds;

  @Override
  public List<Message> handleMessage(final IrcEvent event) {
    final String message = event.getMessage();
    final List<Message> responses = new ArrayList<>();
    if (message.toLowerCase().startsWith(KEYWORD_LOGS)) {
      final String nickname = message.substring(KEYWORD_LOGS.length()).trim();
      LogsCriteria criteria = new LogsCriteria(ds);
      criteria.channel(event.getChannel());
      criteria.updated().order(false);
      IrcUser sender = event.getSender();
      if (nickname.isEmpty()) {
        criteria.query().limit(200);
      } else {
        criteria.nick(nickname);
        criteria.query().limit(50);
      }
      for (Logs logs : criteria.query().fetch()) {
        responses.add(new Message(sender, event,
            String.format("%s <%s> %s", logs.getUpdated().format(DateTimeFormatter.ofPattern("HH:mm")),
                logs.getNick(), logs.getMessage())
        ));
      }
      if (responses.isEmpty()) {
        if (nickname.isEmpty()) {
          responses.add(new Message(sender, event, "No logs found."));
        } else {
          responses.add(new Message(sender, event, "No logs found for nick: " + nickname));
        }
      }
      Collections.reverse(responses);
    }
    return responses;
  }
}
