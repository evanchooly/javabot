package javabot.commands;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public abstract class BaseCommand implements Command {
    private static final Logger log = LoggerFactory.getLogger(BaseCommand.class);
    @Autowired
    ApplicationContext context;

    @Override
    public boolean canHandle(String event) {
        return false;
    }

    public abstract void execute(List<String> args, final List<Message> responses, Javabot bot, BotEvent event);

    public String getUsage() {
        final Options options = getOptions();
        final StringBuilder builder = new StringBuilder("usage: " + getCommandName());
        for (final Object o : options.getOptions()) {
            final Option option = (Option) o;
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
        final Options options = new Options();
        for (final Field field : getClass().getDeclaredFields()) {
            final Param annotation = field.getAnnotation(Param.class);
            if (annotation != null) {
                final String name = "".equals(annotation.name()) ? field.getName() : annotation.name();
                final String value = !StringUtils.isEmpty(annotation.defaultValue()) ? annotation.defaultValue() : null;
                final Option option = new Option(name, true, null) {
                    @Override
                    public String getValue() {
                        final String optValue = super.getValue();
                        return optValue == null ? value : optValue;
                    }
                };
                option.setRequired(annotation.required() && !annotation.primary());
                options.addOption(option);
            }
        }
        return options;
    }

    public final void parse(final List<String> args) throws ParseException {
        final Options options = getOptions();
        final CommandLineParser parser = new GnuParser();
        final CommandLine line = parser.parse(options, args.toArray(new String[args.size()]));
        try {
            final Iterator iterator = line.iterator();
            while (iterator.hasNext()) {
                final Option option = (Option) iterator.next();
                final Field field = getClass().getDeclaredField(option.getOpt());
                field.set(this, option.getValue());
            }
            final List argList = line.getArgList();
            if(argList.size() == 2) {
                Field field = getPrimaryParam();
                if(field == null) {
                    throw new ParseException("Required option missing from " + getCommandName());
                }
                field.set(this, argList.get(1));
            } else if(argList.size() > 2) {
                throw new ParseException("Too many options given to " + getCommandName());
            }
        } catch (NoSuchFieldException e) {
            log.error(e.getMessage(), e);
            throw new ParseException(e.getMessage());
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
            throw new ParseException(e.getMessage());
        }
    }

    private Field getPrimaryParam() {
        for (final Field field : getClass().getDeclaredFields()) {
            final Param annotation = field.getAnnotation(Param.class);
            if (annotation != null && annotation.primary()) {
                return field;
            }
        }
        return null;
    }

    public String getCommandName() {
        String name = getClass().getSimpleName();
        name = name.substring(0, 1).toLowerCase() + name.substring(1);
        return name;
    }

}
