package javabot.commands;

import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import org.apache.commons.cli.ParseException;

/**
 * Created Dec 17, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public interface Command {
    void execute(final List<Message> responses, Javabot bot, BotEvent event);

    void parse(List<String> args) throws ParseException;
}
