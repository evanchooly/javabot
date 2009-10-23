package javabot.commands;

import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.ApiDao;
import javabot.javadoc.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Created Jan 9, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class DropApi extends BaseCommand {
    @Autowired
    private ApiDao dao;
    @Autowired
    private ApplicationContext context;
    @Param
    String name;

    @Override
    @SuppressWarnings("IOResourceOpenedButNotSafelyClosed")
    public void execute(final List<Message> responses, final Javabot bot, final BotEvent event) {
        final String destination = event.getChannel();
        final Api api = dao.find(name);
        if (api != null) {
            drop(responses, event, destination, api, dao);
        } else {
            responses.add(new Message(destination, event,
                String.format("I don't have javadoc for %s anyway, %s", name, event.getSender())));

        }
    }

    private void drop(final List<Message> responses, final BotEvent event, final String destination, final Api api,
        final ApiDao apiDao) {
        responses.add(new Message(destination, event, String.format("removing old %s javadoc", api.getName())));
        apiDao.delete(api);
        responses.add(new Message(destination, event, String.format("done removing javadoc for %s", api.getName())));
    }
}