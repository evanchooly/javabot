package javabot.operations;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.commands.Command;
import javabot.dao.AdminDao;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created Dec 17, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class AdminOperation extends BotOperation implements ApplicationContextAware {
    public static final List<String> OPERATIONS = Arrays.asList(
        //AddFactoidOperation.class.getName(),
        AdminOperation.class.getName(),
        AolBonicsOperation.class.getName(),
        DaysToChristmasOperation.class.getName(),
        DaysUntilOperation.class.getName(),
        DictOperation.class.getName(),
        ForgetFactoidOperation.class.getName(),
        GoogleOperation.class.getName(),
        IgnoreOperation.class.getName(),
        InfoOperation.class.getName(),
        JavadocOperation.class.getName(),
        JSROperation.class.getName(),
        KarmaChangeOperation.class.getName(),
        KarmaReadOperation.class.getName(),
        LeaveOperation.class.getName(),
        LiteralOperation.class.getName(),
        Magic8BallOperation.class.getName(),
        NickometerOperation.class.getName(),
        QuitOperation.class.getName(),
        Rot13Operation.class.getName(),
        SayOperation.class.getName(),
        SeenOperation.class.getName(),
        SpecialCasesOperation.class.getName(),
        StatsOperation.class.getName(),
        TellOperation.class.getName(),
        TimeOperation.class.getName(),
        ShunOperation.class.getName(),
        UnixCommandOperation.class.getName()
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
    public List<Message> handleMessage(final BotEvent event) {
        final List<Message> messages = new ArrayList<Message>();
        final String message = event.getMessage();
        final String channel = event.getChannel();
        if (message.startsWith(ADMIN_PREFIX)) {
            if(isAdmin(event)) {
                final String[] params = message.substring(ADMIN_PREFIX.length()).trim().split(" ");
                final List<String> args = new ArrayList<String>(Arrays.asList(params));
                if (!args.isEmpty()) {
                    try {
                        final Command command = getCommand(args);
                        args.remove(0);
                        messages.addAll(command.execute(getBot(), event, args));
                    } catch (ClassNotFoundException e) {
                        messages.add(new Message(channel, event, params[0] + " command not found"));
                        privMessageStackTrace(event, messages, e);
                    } catch (Exception e) {
                        privMessageStackTrace(event, messages, e);
                        messages.add(new Message(channel, event, "Could not execute command: " + params[0]
                            + ", " + e.getMessage()));
                    }
                }
            } else {
                messages.add(new Message(channel, event, event.getSender() + ", you're not an admin"));
            }
        }
        return messages;
    }

    private void privMessageStackTrace(final BotEvent event, final List<Message> messages, final Exception e) {
        final StringWriter writer = new StringWriter();
        final PrintWriter w = new PrintWriter(writer);
        try {
            e.printStackTrace(w);
            for(final String line : writer.toString().split("\\n")) {
                messages.add(new Message(event.getSender(), event, line));
            }
        } finally {
            w.close();
        }
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

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
