package javabot.commands;

import java.io.StringWriter;
import java.util.ArrayList;
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
public class Javadoc implements Command {
    @Autowired
    private ApiDao dao;
    @Autowired
    private ApplicationContext context;

    @Override
    @SuppressWarnings("IOResourceOpenedButNotSafelyClosed")
    public List<Message> execute(final Javabot bot, final BotEvent event, final List<String> args) {
        List<Message> list = new ArrayList<Message>();
        if (args.size() < 2) {
            list.add(new Message(event.getChannel(), event, "usage: javadoc <name> <url> [<packages>]"));
            list.add(new Message(event.getChannel(), event,
                "usage: packages are optional.  if not given all packages found will be documented"));
        } else {
            final String name = args.remove(0);
            Api api = dao.find(name);
            if (api != null) {
                bot.postMessage(new Message(event.getChannel(), event, "removing old " + name + " javadoc"));
                dao.delete(api);
                bot.postMessage(new Message(event.getChannel(), event, "done removing old " + name + " javadoc"));
            }
            final String urlString = args.remove(0);
            api = new Api(name, urlString);
            dao.save(api);
            final JavadocParser parser = new JavadocParser();
            context.getAutowireCapableBeanFactory().autowireBean(parser);
            parser.parse(api, args, new StringWriter() {
                @Override
                public void write(final String line) {
                    bot.postMessage(new Message(event.getSender(), event, line));
                }
            });
            list.add(new Message(event.getChannel(), event, "adding javadoc for " + name));
        }
        return list;
    }
}