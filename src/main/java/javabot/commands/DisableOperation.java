package javabot.commands;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;

/**
 * Created Jan 26, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class DisableOperation extends OperationsCommand {
    @Override
    public List<Message> execute(final Javabot bot, final BotEvent event, final List<String> args) {
        final List<Message> messages = new ArrayList<Message>();
        if (args.isEmpty()) {
            bot.postMessage(new Message(event.getChannel(), event, "usage: disableOperation <name>"));
            bot.postMessage(new Message(event.getChannel(), event,
                "usage: use admin listOperations to see list of options"));
        } else {
            final String name = args.get(0);
            if(bot.removeOperation(name)) {
                bot.postMessage(new Message(event.getChannel(), event, name + " successfully disabled."));
                listCurrent(bot, event);
            } else {
                bot.postMessage(new Message(event.getChannel(), event, name + " not disabled.  Either it is not running"
                    + " or it's not a valid name.  see listOperations for details."));
            }
        }
        return messages;
    }
}