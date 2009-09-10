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
    @Param
    String name;

    @Override
    public void execute(final Javabot bot, final BotEvent event) {
        if (bot.removeOperation(name)) {
            bot.postMessage(new Message(event.getChannel(), event, name + " successfully disabled."));
            listCurrent(bot, event);
        } else {
            bot.postMessage(new Message(event.getChannel(), event, name + " not disabled.  Either it is not running"
                + " or it's not a valid name.  see listOperations for details."));
        }
    }
}