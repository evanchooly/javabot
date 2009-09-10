package javabot.commands;

import java.util.List;
import java.util.Iterator;
import java.lang.reflect.Field;

import javabot.Javabot;
import javabot.BotEvent;
import javabot.Message;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseCommand implements Command {
    private static final Logger log = LoggerFactory.getLogger(BaseCommand.class);
    @Autowired
    ApplicationContext context;

    @Override
    public void execute(Javabot bot, BotEvent event, List<String> args) {
        bot.postMessage(new Message(event.getChannel(), event,
            "execute(Javabot bot, BotEvent event, List<String> args) not implemented for " + getCommandName()));
    }

    public abstract void execute(Javabot bot, BotEvent event);

    public String getUsage() {
        Options options = getOptions();
        StringBuilder builder = new StringBuilder("usage: " + getCommandName());
        for (final Object o : options.getOptions()) {
            Option option = (Option) o;
            builder.append(" ");
            if (!option.isRequired()) {
                builder.append(" [");
            }
            builder.append("--" + option.getOpt());
            if (option.getValue() != null) {
                builder.append("=" + option.getValue());
            } else {
                builder.append("=<" + option.getOpt() + ">");
            }
            if (!option.isRequired()) {
                builder.append("]");
            }
        }
        return builder.toString();
    }

    public Options getOptions() {
        Options options = new Options();
        for (Field field : getClass().getDeclaredFields()) {
            final Param annotation = field.getAnnotation(Param.class);
            if (annotation != null) {
                final String name = "".equals(annotation.name()) ? field.getName() : annotation.name();
                Option option = new Option(name, true, null);
                option.setRequired(annotation.required());
                options.addOption(option);
            }
        }
        return options;
    }

    public void parse(final List<String> args) throws ParseException {
        final Options options = getOptions();
        CommandLineParser parser = new GnuParser();
        final CommandLine line = parser.parse(options, args.toArray(new String[args.size()]));
        final Iterator iterator = line.iterator();
        try {
            while (iterator.hasNext()) {
                Option option = (Option) iterator.next();
                getClass().getDeclaredField(option.getOpt()).set(this, option.getValue());
            }
        } catch (NoSuchFieldException e) {
            log.error(e.getMessage(), e);
            throw new ParseException(e.getMessage());
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
            throw new ParseException(e.getMessage());
        }
    }

    public String getCommandName() {
        String name = getClass().getSimpleName();
        name = name.substring(0, 1).toLowerCase() + name.substring(1);
        return name;
    }
}
