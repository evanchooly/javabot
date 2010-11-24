package javabot.operations;

import com.antwerkz.maven.SPI;
import javabot.BotEvent;
import javabot.Message;
import javabot.commands.BaseCommand;
import javabot.commands.Command;
import org.apache.commons.cli.MissingOptionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Created Dec 17, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@SPI(BotOperation.class)
public class AdminOperation extends BotOperation {
    private static final Logger log = LoggerFactory.getLogger(AdminOperation.class);
    private static final String ADMIN_PREFIX = "admin ";

    @Override
    public boolean isStandardOperation() {
        return true;
    }

    @Override
    public final List<Message> handleMessage(final BotEvent event) {
        final String message = event.getMessage();
        final String channel = event.getChannel();
        final List<Message> responses = new ArrayList<Message>();
        if (message.startsWith(ADMIN_PREFIX)) {
            if (isAdminUser(event)) {
                final String[] params = message.substring(ADMIN_PREFIX.length()).trim().split(" ");
                final List<String> args = new ArrayList<String>(Arrays.asList(params));
                if (!args.isEmpty()) {
                    if ("-list".equals(args.get(0))) {
                        final StringBuilder builder = new StringBuilder();
                        for (final Command command : listCommands()) {
                            if (builder.length() != 0) {
                                builder.append(", ");
                            }
                            final String name = command.getClass().getSimpleName();
                            builder
                                    .append(name.substring(0, 1).toLowerCase())
                                    .append(name.substring(1));
                        }
                        responses.add(new Message(channel, event, event.getSender() + ", I know of the following"
                                + " commands: " + builder));
                    } else {
                        methods(responses, event, channel, params, args);
                    }
                }
            } else {
                responses.add(new Message(channel, event, event.getSender() + ", you're not an admin"));
            }
        }
        return responses;
    }

    private void methods(final List<Message> responses, final BotEvent event, final String channel,
                         final String[] params, final List<String> args) {
        try {
            final Iterator<Command> commands = listCommands().iterator();
            while (responses.isEmpty() && commands.hasNext()) {
                final Command command = commands.next();
                try {
                    if(command.canHandle(args.get(0))) {
                        command.parse(args);
                        command.execute(args, responses, getBot(), event);
                    }
                } catch (MissingOptionException moe) {
                    responses.add(new Message(channel, event, ((BaseCommand) command).getUsage()));
                }
            }
        } catch (Exception e) {
            privMessageStackTrace(e);
            responses.add(new Message(channel, event, "Could not execute command: " + params[0]
                    + ", " + e.getMessage()));
        }
    }

    private void privMessageStackTrace(final Exception e) {
        log.debug(e.getMessage(), e);
    }

    public List<Command> listCommands() {
        final ServiceLoader<Command> loader = ServiceLoader.load(Command.class);
        final List<Command> list = new ArrayList<Command>();
        for (final Command command : loader) {
            getBot().inject(command);
            list.add(command);
        }
        return list;
    }
}