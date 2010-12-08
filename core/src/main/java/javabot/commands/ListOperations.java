package javabot.commands;

import java.util.ArrayList;
import java.util.List;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.operations.BotOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created Jan 26, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@SPI({AdminCommand.class})
public class ListOperations extends OperationsCommand {
    private static final Logger log = LoggerFactory.getLogger(ListOperations.class);

    @Override
    public String getName() {
        return "ListOperations";
    }

    @Override
    public List<Message> execute(final Javabot bot, final IrcEvent event) {
        final List<Message> responses = new ArrayList<Message>();
        responses.add(new Message(event.getChannel(), event, "I know of the following operations:"));
        responses.add(new Message(event.getChannel(), event,
            StringUtils.join(BotOperation.list().iterator(), ",")));

        listCurrent(responses, bot, event);
        responses.add(new Message(event.getChannel(), event, "use admin enableOperation or disableOperation to turn"
            + " operations on or off"));
        return responses;
    }
}