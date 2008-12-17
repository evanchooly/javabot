package javabot.commands;

import java.util.List;

import javabot.Message;
import javabot.Javabot;
import javabot.BotEvent;

/**
 * Created Dec 17, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public interface Command {
    List<Message> execute(Javabot bot, BotEvent event, List<String> args);
}
