package javabot.commands;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javabot.IrcEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.operations.BotOperation;
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

public abstract class AdminCommand extends BotOperation {
    private static final Logger log = LoggerFactory.getLogger(AdminCommand.class);
    @Autowired
    protected ApplicationContext context;
    protected List<String> args;

    @Override
    public final List<Message> handleMessage(final IrcEvent event) {
        String message = event.getMessage();
        final String channel = event.getChannel();
        final List<Message> responses = new ArrayList<Message>();
        if (message.toLowerCase().startsWith("admin ")) {
            if (isAdminUser(event)) {
                message = message.substring(6);
                final String[] split = message.split(" ");
                if (canHandle(split[0])) {
                    try {
                        parse(Arrays.asList(split));
                        responses.addAll(execute(getBot(), event));
                    } catch (ParseException e) {
                        log.error(e.getMessage(), e);
                        responses.add(new Message(channel, event, "An error occurred processing this request."));
                    }
                }
            } else {
                responses.add(new Message(channel, event, event.getSender() + ", you're not an admin"));
            }
        }
        return responses;
    }

    public boolean canHandle(String message) {
        try {
            return message.equalsIgnoreCase(getClass().getSimpleName());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public abstract List<Message> execute(Javabot bot, IrcEvent event);

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

    public final void parse(final List<String> params) throws ParseException {
        final Options options = getOptions();
        final CommandLineParser parser = new GnuParser();
        final CommandLine line = parser.parse(options, params.toArray(new String[params.size()]));
        //noinspection unchecked
        args = line.getArgList();
        try {
            final Iterator iterator = line.iterator();
            while (iterator.hasNext()) {
                final Option option = (Option) iterator.next();
                final Field field = getClass().getDeclaredField(option.getOpt());
                field.set(this, option.getValue());
            }
            if ("admin".equals(args.get(0))) {
                args.remove(0);
            }
            if (args.size() == 2) {
                Field field = getPrimaryParam();
                if (field == null) {
                    throw new ParseException("Required option missing from " + getCommandName());
                }
                field.set(this, args.get(1));
            } else if (args.size() > 2) {
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

    @Override
    public String toString() {
        return String.format("%s [admin]", getName());
    }
}
