package javabot.commands;

import java.util.List;

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
    public void execute(final List<Message> responses, final Javabot bot, final BotEvent event) {
        if (bot.addOperation(name)) {
            responses.add(new Message(event.getChannel(), event, name + " successfully enabled."));
            listCurrent(responses, bot, event);
        } else {
            responses.add(new Message(event.getChannel(), event, name + " not enabled.  Either it is already running"
                + " or it's not a valid name.  see listOperations for details."));
        }
    }
}
