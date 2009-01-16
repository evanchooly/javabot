package javabot.commands;

import java.io.StringWriter;
import java.util.Collections;
import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.ApiDao;
import javabot.javadoc.JavadocParser;
import javabot.javadoc.Api;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created Jan 9, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class Javadoc implements Command {
    @Autowired
    private ApiDao dao;

    @Override
    @SuppressWarnings("IOResourceOpenedButNotSafelyClosed")
    public List<Message> execute(final Javabot bot, final BotEvent event, final List<String> args) {
        if (args.size() < 2) {
            bot.postMessage(new Message(event.getChannel(), event, "usage: javadoc <name> <url> [<packages>]"));
            bot.postMessage(new Message(event.getChannel(), event,
                "usage: packages are optional.  if not given all packages found will be documented"));
        } else {
            final String name = args.remove(0);
            Api api = dao.find(name);
            if (api != null) {
                dao.delete(api);
            }
            final String urlString = args.remove(0);
            api = new Api(name, urlString);
            final JavadocParser parser = new JavadocParser();
            parser.parse(api, args, new StringWriter() {
                @Override
                public void write(final String line) {
                    bot.postMessage(new Message(event.getSender(), event, line));
                }
            });
        }
        return Collections.emptyList();
    }
}