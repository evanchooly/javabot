package javabot.commands;

import java.util.ArrayList;
import java.util.List;

import com.antwerkz.maven.SPI;
import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.ApiDao;
import javabot.javadoc.Api;
import javabot.operations.BotOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Created Jan 9, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@SPI({BotOperation.class, AdminCommand.class})
public class InfoApi extends AdminCommand {
    @Autowired
    private ApiDao dao;
    @Autowired
    private ApplicationContext context;
    @Param
    String name;

    @Override
    @SuppressWarnings("IOResourceOpenedButNotSafelyClosed")
    public List<Message> execute(final Javabot bot, final BotEvent event) {
        final List<Message> responses = new ArrayList<Message>();
        final String destination = event.getChannel();
        final Api api = dao.find(name);
        if (api != null) {
            responses.add(new Message(destination, event, String.format(
                "The %s API can be found at %s.  I know about these packages from that API:  %s",
                api.getName(), api.getBaseUrl(), api.getPackages() == null ? "*" : api.getPackages())));
        } else {
            responses.add(new Message(destination, event, String.format(
                "I don't have javadoc for %s, %s", name, event.getSender())));
        }
        return responses;
    }
}