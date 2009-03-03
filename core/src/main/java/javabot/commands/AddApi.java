package javabot.commands;

import java.io.StringWriter;
import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.ApiDao;
import javabot.javadoc.Api;
import javabot.javadoc.JavadocParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Created Jan 9, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class AddApi implements Command {
    @Autowired
    private ApiDao dao;
    @Autowired
    private ApplicationContext context;

    @Override
    @SuppressWarnings("IOResourceOpenedButNotSafelyClosed")
    public void execute(final Javabot bot, final BotEvent event, final List<String> args) {
        final String destination = event.getChannel();
        boolean existing = false;
        if (args.size() < 2) {
            if (args.size() == 1) {
                existing = dao.find(args.get(0)) != null;
            }
            if (args.size() < 1 || !existing) {
                bot.postMessage(new Message(destination, event, "usage: addApi <name> [<url>] [<packages>]"));
                bot.postMessage(new Message(destination, event,
                    "usage: if the url is not specified, the bot will try to reprocess an existing api.  packages are"
                        + " optional.  if not given all packages found will be documented"));
            }
        }
        if (args.size() >= 2 || existing) {
            final String name = args.remove(0);
            Api api = dao.find(name);
            if (existing && api != null) {
                DropApi.drop(bot, event, destination, api, dao);
            }
            final String urlString = existing ? api.getBaseUrl() : args.remove(0);
            api = new Api(name, urlString);
            dao.save(api);
            final JavadocParser parser = new JavadocParser();
            context.getAutowireCapableBeanFactory().autowireBean(parser);
            parser.parse(api, args, new StringWriter() {
                @Override
                public void write(final String line) {
                    bot.postMessage(new Message(event.getChannel(), event, line));
                }
            });
            bot.postMessage(new Message(destination, event, "done adding javadoc for " + name));
        }
    }
}