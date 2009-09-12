package javabot.commands;

import java.io.StringWriter;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.ApiDao;
import javabot.javadoc.Api;
import javabot.javadoc.JavadocParser;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created Jan 9, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class ReprocessApi extends BaseCommand {
    @Autowired
    private ApiDao dao;
    @Param
    String name;
    @Param(required = false)
    String packages;

    @Override
    @SuppressWarnings("IOResourceOpenedButNotSafelyClosed")
    public void execute(final Javabot bot, final BotEvent event) {
        final String destination = event.getChannel();
        Api api = dao.find(name);
        if (api == null) {
            bot.postMessage(new Message(destination, event, "I don't know anything about " + name));
        } else {
            drop(bot, event, destination, api, dao);
            api = new Api(name, api.getBaseUrl(), packages == null ? api.getPackages() : packages, api.getZipLocations());
            dao.save(api);
            final JavadocParser parser = new JavadocParser();
            context.getAutowireCapableBeanFactory().autowireBean(parser);
            parser.parse(api, new StringWriter() {
                @Override
                public void write(final String line) {
                    bot.postMessage(new Message(event.getSender(), event, line));
                }
            });
            bot.postMessage(new Message(destination, event, "done reprocessing javadoc for " + name));
        }
    }

    private void drop(final Javabot bot, final BotEvent event, final String destination, final Api api,
        final ApiDao apiDao) {
        bot.postMessage(new Message(destination, event, String.format("removing old %s javadoc for reprocessing",
            api.getName())));
        apiDao.delete(api);
        bot.postMessage(new Message(destination, event, String.format(
            "done removing old %s javadoc", api.getName())));
    }
}