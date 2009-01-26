package javabot.commands;

import java.util.List;

import javabot.Javabot;
import javabot.BotEvent;
import javabot.Message;

/**
 * Created Jan 26, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public abstract class OperationsCommand implements Command {
    protected void listCurrent(final Javabot bot, final BotEvent event) {
        bot.postMessage(new Message(event.getChannel(), event, "I am currently running the following operations:"));
        bot.postMessage(new Message(event.getChannel(), event, stringify(bot.listActiveOperations())));
    }

    protected String stringify(final List<String> list) {
        final StringBuilder builder = new StringBuilder();
        for (final String operation : list) {
            if(builder.length() != 0) {
                builder.append(", ");
            }
            builder.append(operation);
        }

        return builder.toString();
    }
}
