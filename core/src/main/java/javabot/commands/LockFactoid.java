package javabot.commands;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.FactoidDao;
import javabot.model.Factoid;

/**
 * Created Jan 26, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@SPI({AdminCommand.class})
public class LockFactoid extends AdminCommand {
  @Param(primary = true)
  String name;

  @Inject
  private FactoidDao dao;

  @Override
  public boolean canHandle(String message) {
    return "lock".equals(message) || "unlock".equals(message);
  }

  @Override
  public List<Message> execute(final Javabot bot, final IrcEvent event) {
    final List<Message> responses = new ArrayList<Message>();
    final String command = args.get(0);
    if ("lock".equals(command) || "unlock".equals(command)) {
      final Factoid factoid = dao.getFactoid(name);
      if(factoid == null) {
        responses.add(new Message(event.getChannel(), event, "I don't know anything about " + name));
      } else if ("lock".equals(command)) {
        factoid.setLocked(true);
        dao.save(factoid);
        responses.add(new Message(event.getChannel(), event, name + " locked."));
      } else if ("unlock".equals(command)) {
        factoid.setLocked(false);
        dao.save(factoid);
        responses.add(new Message(event.getChannel(), event, name + " unlocked."));
      }
    }
    return responses;
  }
}
