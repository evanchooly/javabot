package javabot.commands;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.ConfigDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.StringUtils;

public class Config extends BaseCommand {
    @Autowired
    private ConfigDao dao;
    @Param(required = false)
    String property;
    @Param(required = false)
    String value;

    @Override
    public void execute(final Javabot bot, final BotEvent event) {
        final javabot.model.Config config = dao.get();
        if (StringUtils.isEmpty(property)) {
            bot.postMessage(new Message(event.getSender(), event, config.toString()));
        } else {
            try {
                final String name = property.substring(0, 1).toUpperCase() + property.substring(1);
                final Method get = config.getClass().getDeclaredMethod("get" + name);
                final Class<?> type = get.getReturnType();
                final Method set = config.getClass().getDeclaredMethod("set" + name, type);
                try {
                    set.invoke(config, type.equals(String.class) ? value.trim() : Integer.parseInt(value));
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
                bot.postMessage(new Message(event.getSender(), event, "I don't know of a property named " + property));
            }
        }
    }
}
