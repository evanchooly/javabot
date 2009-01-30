package javabot.commands;

import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;

/**
 * Created Dec 17, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public interface Command {
    void execute(Javabot bot, BotEvent event, List<String> args);
}
