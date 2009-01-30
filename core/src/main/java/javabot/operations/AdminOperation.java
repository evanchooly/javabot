package javabot.operations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.commands.*;
import javabot.dao.AdminDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Created Dec 17, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class AdminOperation extends BotOperation {
    private static final Logger log = LoggerFactory.getLogger(AdminOperation.class);
    public static final List<String> COMMANDS = Arrays.asList(
        AddAdmin.class.getSimpleName(),
        AddApi.class.getSimpleName(),
        AddChannel.class.getSimpleName(),
        DisableOperation.class.getSimpleName(),
        DropApi.class.getSimpleName(),
        DropChannel.class.getSimpleName(),
        EnableOperation.class.getSimpleName(),
        ListChannels.class.getSimpleName(),
        ListOperations.class.getSimpleName()
    );
    private static final String ADMIN_PREFIX = "admin ";
    @Autowired
    private ApplicationContext context;
    @Autowired
    private AdminDao dao;

    public AdminOperation(final Javabot javabot) {
        super(javabot);
    }

    @Override
    public boolean handleMessage(final BotEvent event) {
        final String message = event.getMessage();
        final String channel = event.getChannel();
        boolean handled = false;
        if (message.startsWith(ADMIN_PREFIX)) {
            handled = true;
            if(isAdmin(event)) {
                final String[] params = message.substring(ADMIN_PREFIX.length()).trim().split(" ");
                final List<String> args = new ArrayList<String>(Arrays.asList(params));
                if (!args.isEmpty()) {
                    if("-list".equals(args.get(0))) {
                        final StringBuilder builder = new StringBuilder();
                        for (final String command : COMMANDS) {
                            if(builder.length() != 0) {
                                builder.append(", ");
                            }
                            builder
                                .append(command.substring(0, 1).toLowerCase())
                                .append(command.substring(1));
                        }
                        getBot().postMessage(new Message(channel, event, event.getSender() + ", I know of the following"
                            + " commands: " + builder));
                    } else {
                        methods(event, channel, params, args);
                    }
                }
            } else {
                getBot().postMessage(new Message(channel, event, event.getSender() + ", you're not an admin"));
            }
        }
        return handled;
    }

    private void methods(final BotEvent event, final String channel, final String[] params, final List<String> args) {
        try {
            final Command command = getCommand(args);
            args.remove(0);
            command.execute(getBot(), event, args);
        } catch (ClassNotFoundException e) {
            getBot().postMessage(new Message(channel, event, params[0] + " command not found"));
            privMessageStackTrace(event, e);
        } catch (Exception e) {
            privMessageStackTrace(event, e);
            getBot().postMessage(new Message(channel, event, "Could not execute command: " + params[0]
                + ", " + e.getMessage()));
        }
    }

    private void privMessageStackTrace(final BotEvent event, final Exception e) {
        log.debug(e.getMessage(), e);
//        final StringWriter writer = new StringWriter();
//        final PrintWriter w = new PrintWriter(writer);
//        try {
//            e.printStackTrace(w);
//            for(final String line : writer.toString().split("\\n")) {
//                getBot().postMessage(new Message(event.getSender(), event, line));
//            }
//        } finally {
//            w.close();
//        }
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
}
