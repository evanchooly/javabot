package javabot.operations;

import com.antwerkz.maven.SPI;
import com.google.code.morphia.Datastore;
import javabot.IrcEvent;
import javabot.IrcUser;
import javabot.Message;
import javabot.commands.Param;
import javabot.dao.LogsDao;
import javabot.model.Logs;
import javabot.model.criteria.LogsCriteria;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Gets logs for channel, optionally filtered by a nickname
 *
 * @author StanAccy
 */
@SPI(BotOperation.class)
public class LogsOperation extends BotOperation {
  private static final String KEYWORD_LOGS = "logs";

  @Inject
  private LogsDao logsDao;

  @Inject
  private Datastore ds;

  @Param(defaultValue = "", required = false)
  String limit;
  @Param(defaultValue = "24", required = false)
  String hours;

  @Override
  public List<Message> handleMessage(final IrcEvent event) {
    final String message = event.getMessage();
    final List<Message> responses = new ArrayList<>();
    if (message.toLowerCase().startsWith(KEYWORD_LOGS)) {
      final String nickname = message.substring(KEYWORD_LOGS.length()).trim();
      LogsCriteria criteria = new LogsCriteria(ds);
      criteria.channel(event.getChannel());
      criteria.orderByUpdated(false);
      IrcUser sender = event.getSender();
      int numLogLines = getLimit(nickname.isEmpty(), limit);
      criteria.query().limit(numLogLines);
      if (!nickname.isEmpty()) {
        criteria.nick(nickname);
      }
      int hoursBack = getHoursBack();
      criteria.updated().greaterThan(DateTime.now().minusHours(hoursBack));
      for (Logs logs : criteria.query().fetch()) {
        responses.add(new Message(sender, event,
            String.format("%s <%s> %s", logs.getUpdated().toString("HH:mm"), logs.getNick(), logs.getMessage())));
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

  private int getHoursBack() {
    try {
      return Integer.parseInt(hours);
    } catch (NumberFormatException ignored) {
      // NOP
    }

    return 24;
  }

  static int getLimit(boolean isAllUsers, String limit) {
    try {
      return Integer.parseInt(limit);
    } catch (NumberFormatException ignored) {
      // NOP
    }
    if (isAllUsers) {
      return 100;
    }
    return 25;
  }
}
