package javabot.operations;

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
    private static final String ADMIN_PREFIX = "admin ";
    @Autowired
    private ApplicationContext context;
    @Autowired
    private AdminDao dao;

    public AdminOperation(Javabot javabot) {
        super(javabot);
    }

    @Override
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        String channel = event.getChannel();
        if (message.startsWith(ADMIN_PREFIX)) {
            if(isAdmin(event)) {
                String[] params = message.substring(ADMIN_PREFIX.length()).trim().split(" ");
                List<String> args = new ArrayList<String>(Arrays.asList(params));
                if (!args.isEmpty()) {
                    try {
                        Command command = getCommand(args);
                        args.remove(0);
                        messages.addAll(command.execute(getBot(), event, args));
                    } catch (ClassNotFoundException e) {
                        messages.add(new Message(channel, event, params[0] + " command not found"));
                    } catch (Exception e) {
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

    private boolean isAdmin(BotEvent event) {
        return dao.isAdmin(event.getSender(), event.getHostname());
    }

    @SuppressWarnings({"unchecked"})
    private Command getCommand(List<String> params)
        throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String name = params.get(0);
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        String className = Command.class.getPackage().getName() + "." + name;
        Class<Command> clazz = (Class<Command>) Class.forName(className);
        Command command = clazz.newInstance();
        context.getAutowireCapableBeanFactory().autowireBean(command);
        return command;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
