package javabot.commands;

import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created Jan 26, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class ListOperations extends OperationsCommand implements Command {
    private static final Logger log = LoggerFactory.getLogger(ListOperations.class);

    @Override
    public void execute(final Javabot bot, final BotEvent event, final List<String> args) {
        bot.postMessage(new Message(event.getChannel(), event, "I know of the following operations:"));
        bot.postMessage(new Message(event.getChannel(), event, stringify(Javabot.OPERATIONS)));
        listCurrent(bot, event);
        bot.postMessage(new Message(event.getChannel(), event, "use admin enableOperation or disableOperation to turn"
            + " operations on or off"));
    }
}