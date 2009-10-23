package javabot.operations;

import java.util.List;
import java.util.Collections;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;

public abstract class BotOperation {
    private final Javabot bot;

    public BotOperation(final Javabot javabot) {
        bot = javabot;
    }

    public Javabot getBot() {
        return bot;
    }

    /**
     * Returns a list of BotOperation.Message, empty if the operation was not applicable to the message passed. It
     * should never return null.
     *
     * @param event
     *
     * @return
     */
    public List<Message> handleMessage(final BotEvent event) {
        return Collections.emptyList();
    }

    public List<Message> handleChannelMessage(final BotEvent event) {
        return Collections.emptyList();
    }

    public static String getName(final Class clazz) {
        return clazz.getSimpleName().replaceAll("Operation", "");
    }
}