package javabot.commands;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
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
@SPI({AdminCommand.class})
public class AddApi extends AdminCommand {
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
    public List<Message> execute(final Javabot bot, final IrcEvent event) {
        final List<Message> responses = new ArrayList<Message>();
        final String destination = event.getChannel();
        final Api api = new Api(name, url, packages, zip);
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
        return responses;
    }
}