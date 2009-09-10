package javabot.commands;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import javabot.Message;
import javabot.Javabot;
import javabot.BotEvent;

/**
 * Created Jan 26, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class EnableOperation extends OperationsCommand {
    @Param
    String name;

    @Override
    public void execute(final Javabot bot, final BotEvent event) {
        if (bot.addOperation(name)) {
            bot.postMessage(new Message(event.getChannel(), event, name + " successfully enabled."));
            listCurrent(bot, event);
        } else {
            bot.postMessage(new Message(event.getChannel(), event, name + " not enabled.  Either it is already running"
                + " or it's not a valid name.  see listOperations for details."));
        }
    }
}
