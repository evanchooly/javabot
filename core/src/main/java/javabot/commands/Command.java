package javabot.commands;

import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import org.apache.commons.cli.ParseException;

/**
 * Created Dec 17, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public interface Command {
    void execute(Javabot bot, BotEvent event);

    void parse(List<String> args) throws ParseException;
}
