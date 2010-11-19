package javabot.operations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;

import com.antwerkz.maven.SPI;
import javabot.BotEvent;
import javabot.Message;
import javabot.commands.AddAdmin;
import javabot.commands.AddApi;
import javabot.commands.AddChannel;
import javabot.commands.BaseCommand;
import javabot.commands.Command;
import javabot.commands.Config;
import javabot.commands.DisableOperation;
import javabot.commands.DropApi;
import javabot.commands.DropChannel;
import javabot.commands.EnableOperation;
import javabot.commands.InfoApi;
import javabot.commands.ListChannels;
import javabot.commands.ListOperations;
import javabot.commands.ReprocessApi;
import javabot.dao.AdminDao;
import org.apache.commons.cli.MissingOptionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Created Dec 17, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@SPI(BotOperation.class)
public class AdminOperation extends BotOperation {
    private static final Logger log = LoggerFactory.getLogger(AdminOperation.class);
    private static final String ADMIN_PREFIX = "admin ";
    @Autowired
    private ApplicationContext context;
    @Autowired
    private AdminDao dao;

    @Override
    public boolean isStandardOperation() {
        return true;
    }

    @Override
    public List<Message> handleMessage(final BotEvent event) {
        final String message = event.getMessage();
        final String channel = event.getChannel();
        final List<Message> responses = new ArrayList<Message>();
        if (message.startsWith(ADMIN_PREFIX)) {
            if (isAdmin(event)) {
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
            final Command command = getCommand(args);
            args.remove(0);
            try {
                command.parse(args);
                event.setMessage(event.getMessage().substring("admin ".length()));
                command.execute(responses, getBot(), event);
            } catch (MissingOptionException moe) {
                responses.add(new Message(channel, event, ((BaseCommand) command).getUsage()));
            }
        } catch (ClassNotFoundException e) {
            responses.add(new Message(channel, event, params[0] + " command not found"));
            privMessageStackTrace(e);
        } catch (Exception e) {
            privMessageStackTrace(e);
            responses.add(new Message(channel, event, "Could not execute command: " + params[0]
                + ", " + e.getMessage()));
        }
    }

    private void privMessageStackTrace(final Exception e) {
        log.debug(e.getMessage(), e);
    }

    private boolean isAdmin(final BotEvent event) {
        return dao.isAdmin(event.getSender(), event.getHostname());
    }

    @SuppressWarnings({"unchecked"})
    private Command getCommand(final List<String> params)
        throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String name = params.get(0);
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        final String className = Command.class.getPackage().getName() + "." + name;
        final Class<Command> clazz = (Class<Command>) Class.forName(className);
        final Command command = clazz.newInstance();
        inject(command);
        return command;
    }

    protected void inject(final Command command) {
        context.getAutowireCapableBeanFactory().autowireBean(command);
    }

    public static List<Command> listCommands() {
        final ServiceLoader<Command> loader = ServiceLoader.load(Command.class);
        final List<Command> list = new ArrayList<Command>();
        for (final Command operation : loader) {
            list.add(operation);
        }
        return list;
    }
    
}
