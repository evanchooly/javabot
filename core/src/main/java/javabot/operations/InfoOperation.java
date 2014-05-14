package javabot.operations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import com.antwerkz.maven.SPI;
import static java.lang.String.format;
import javabot.IrcEvent;
import javabot.Message;
import javabot.dao.FactoidDao;
import javabot.model.Factoid;

/**
 * Simple operation to pull who added the factoid and when it was added
 */
@SPI(BotOperation.class)
public class InfoOperation extends BotOperation {
  public static final String INFO_DATE_FORMAT = "dd MMM yyyy' at 'KK:mm";

  @Inject
  private FactoidDao dao;

  @Override
  public List<Message> handleMessage(final IrcEvent event) {
    final String message = event.getMessage().toLowerCase();
    final String channel = event.getChannel();
    final List<Message> responses = new ArrayList<Message>();
    if (message.startsWith("info ")) {
      final String key = message.substring("info ".length());
      final Factoid factoid = dao.getFactoid(key);
      if (factoid != null) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(INFO_DATE_FORMAT);
        LocalDateTime updated = factoid.getUpdated();
        String formatted = formatter.format(updated);
        responses.add(new Message(channel, event, format("%s%s was added by: %s on %s and has a literal value of: %s",
            key, factoid.getLocked() ? "*" : "", factoid.getUserName(), formatted,
            factoid.getValue())));
      } else {
        responses.add(new Message(channel, event, "I have no factoid called \"" + key + "\""));
      }
    }
    return responses;
  }

}