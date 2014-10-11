package javabot.commands;

import com.antwerkz.sofia.Sofia;
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
import org.pircbotx.PircBotX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public abstract class AdminCommand extends BotOperation {
    private static final Logger log = LoggerFactory.getLogger(AdminCommand.class);
    protected List<String> args;

    @Inject
    private Provider<Javabot> javabot;

    @Inject
    private Provider<PircBotX> pircBot;

    public Javabot getJavabot() {
        return javabot.get();
    }

    public PircBotX getIrcBot() {
        return pircBot.get();
    }

    @Override
    public final boolean handleMessage(final Message event) {
        boolean handled = false;
        String message = event.getValue();
        if (message.toLowerCase().startsWith("admin ")) {
            if (isAdminUser(event.getUser())) {
                message = message.substring(6);
                final String[] split = message.split(" ");
                if (canHandle(split[0])) {
                    handled = true;
                    try {
                        parse(new ArrayList<>(Arrays.asList(split)));
                        execute(event);
                    } catch (ParseException e) {
                        log.error(e.getMessage(), e);
                        getBot().postMessage(null, event.getUser(), Sofia.adminParseFailure(e.getMessage()), event.isTell());
                    }
                }
            } else {
                getBot().postMessage(event.getChannel(), event.getUser(), Sofia.notAdmin(event.getUser().getNick()), event.isTell());
                handled = true;
            }
        }
        return handled;
    }

    public boolean canHandle(String message) {
        try {
            return message.equalsIgnoreCase(getClass().getSimpleName());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public abstract void execute(Message event);

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

    @SuppressWarnings("unchecked")
    public final void parse(final List<String> params) throws ParseException {
        int index = 2;
        while (index < params.size()) {
            if (!params.get(index).startsWith("-")) {
                params.set(index - 1, params.get(index - 1) + " " + params.remove(index));
            } else {
                index++;
            }
        }
        final Options options = getOptions();
        final CommandLineParser parser = new GnuParser();
        final CommandLine line = parser.parse(options, params.toArray(new String[params.size()]));
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
        } catch (NoSuchFieldException | IllegalAccessException e) {
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
