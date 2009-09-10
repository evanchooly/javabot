package javabot.commands;

import javabot.Javabot;
import javabot.BotEvent;
import javabot.Message;
import org.apache.commons.lang.StringUtils;

/**
 * Created Jan 26, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public abstract class OperationsCommand extends BaseCommand {
    protected void listCurrent(final Javabot bot, final BotEvent event) {
        bot.postMessage(new Message(event.getChannel(), event, "I am currently running the following operations:"));
        bot.postMessage(new Message(event.getChannel(), event, StringUtils.join(bot.listActiveOperations(), ", ")));
    }
}