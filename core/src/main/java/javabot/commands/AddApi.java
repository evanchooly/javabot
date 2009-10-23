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

/**
 * Created Jan 9, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class AddApi extends BaseCommand {
    @Autowired
    private ApiDao dao;
    
    @Param
    String name;
    @Param
    String url;
    @Param
    String zip;
    @Param
    String packages;

    @Override
    @SuppressWarnings("IOResourceOpenedButNotSafelyClosed")
    public void execute(final List<Message> responses, final Javabot bot, final BotEvent event) {
        final String destination = event.getChannel();
        Api api = new Api(name, url, packages, zip);
        dao.save(api);
        final JavadocParser parser = new JavadocParser();
        context.getAutowireCapableBeanFactory().autowireBean(parser);
        parser.parse(api, new StringWriter() {
            @Override
            public void write(final String line) {
                responses.add(new Message(event.getChannel(), event, line));
            }
        });
        responses.add(new Message(destination, event, "done adding javadoc for " + name));
    }
}