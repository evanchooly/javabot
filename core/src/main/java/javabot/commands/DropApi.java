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
public class DropApi implements Command {
    @Autowired
    private ApiDao dao;
    @Autowired
    private ApplicationContext context;

    @Override
    @SuppressWarnings("IOResourceOpenedButNotSafelyClosed")
    public void execute(final Javabot bot, final BotEvent event, final List<String> args) {
        final String destination = event.getChannel();
        if (args.size() != 1) {
            bot.postMessage(new Message(destination, event, "usage: dropApi <name>"));
        } else {
            final String name = args.remove(0);
            final Api api = dao.find(name);
            if (api != null) {
                drop(bot, event, destination, api, dao);
            } else {
                bot.postMessage(new Message(destination, event, String.format(
                    "I don't have javadoc for %s anyway, %s", name, event.getSender())));

            }
        }
    }

    public static void drop(final Javabot bot, final BotEvent event, final String destination, final Api api,
        final ApiDao apiDao) {
        bot.postMessage(new Message(destination, event, String.format(
            "removing old %s javadoc", api.getName())));
        apiDao.delete(api);
        bot.postMessage(new Message(destination, event, String.format(
            "done removing old %s javadoc", api.getName())));
    }
}