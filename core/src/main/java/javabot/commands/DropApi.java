package javabot.commands;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.ApiDao;
import javabot.javadoc.Api;

/**
 * Created Jan 9, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@SPI({AdminCommand.class})
public class DropApi extends AdminCommand {
  @Inject
  private ApiDao dao;
  @Param
  String name;

  @Override
  @SuppressWarnings("IOResourceOpenedButNotSafelyClosed")
  public List<Message> execute(final Javabot bot, final IrcEvent event) {
    final List<Message> responses = new ArrayList<Message>();
    final String destination = event.getChannel();
    final Api api = dao.find(name);
    if (api != null) {
      drop(responses, event, destination, api, dao);
    } else {
      responses.add(new Message(destination, event,
          String.format("I don't have javadoc for %s anyway, %s", name, event.getSender())));
    }
    return responses;
  }

  private void drop(final List<Message> responses, final IrcEvent event, final String destination, final Api api,
      final ApiDao apiDao) {
    responses.add(new Message(destination, event, String.format("removing old %s javadoc", api.getName())));
    apiDao.delete(api);
    responses.add(new Message(destination, event, String.format("done removing javadoc for %s", api.getName())));
  }
}