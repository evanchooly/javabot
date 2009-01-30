package javabot.operations;

import javabot.BotEvent;
import javabot.Javabot;

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
    public boolean handleMessage(final BotEvent event) {
        return false;
    }

    public boolean handleChannelMessage(final BotEvent event) {
        return false;
    }

    public static String getName(final Class clazz) {
        return clazz.getSimpleName().replaceAll("Operation", "");
    }
}