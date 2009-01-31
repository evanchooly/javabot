package javabot.commands;

import java.util.List;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.ConfigDao;
import org.springframework.beans.factory.annotation.Autowired;

public class Config implements Command {
    @Autowired
    private ConfigDao dao;

    @Override
    public void execute(final Javabot bot, final BotEvent event, final List<String> args) {
        final javabot.model.Config config = dao.get();
        if (args.isEmpty()) {
            bot.postMessage(new Message(event.getSender(), event, config.toString()));
        } else {
            final String propName = args.remove(0);
            final StringBuilder value = new StringBuilder();
            while (!args.isEmpty()) {
                value
                    .append(args.remove(0))
                    .append(" ");
            }
            try {
                final String name = propName.substring(0, 1).toUpperCase() + propName.substring(1);
                final Method get = config.getClass().getDeclaredMethod("get" + name);
                final Class<?> type = get.getReturnType();
                final Method set = config.getClass().getDeclaredMethod("set" + name, type);
                try {
                    set.invoke(config,
                        type.equals(String.class) ? value.toString().trim() : Integer.parseInt(value.toString()));
                    dao.save(config);
                    bot.loadConfig(config);
                } catch (IllegalAccessException e) {
                    bot.postMessage(new Message(event.getSender(), event, e.getMessage()));
                } catch (InvocationTargetException e) {
                    bot.postMessage(new Message(event.getSender(), event, e.getMessage()));
                } catch (NumberFormatException e) {
                    bot.postMessage(new Message(event.getSender(), event, e.getMessage()));
                }
            } catch (NoSuchMethodException e) {
                bot.postMessage(new Message(event.getSender(), event, "I don't know of a property named " + propName));
            }
        }
    }
}
