package javabot.operations;

import javabot.IrcEvent;
import javabot.IrcUser;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.AdminDao;

import javax.inject.Inject;
import java.util.*;

public abstract class BotOperation {
    static Random random = new Random();
    private Javabot bot;
    @Inject
    private AdminDao dao;

    public static List<BotOperation> list() {
        final ServiceLoader<BotOperation> loader = ServiceLoader.load(BotOperation.class);
        final List<BotOperation> list = new ArrayList<BotOperation>();
        for (final BotOperation operation : loader) {
            list.add(operation);
        }
        return list;
    }

    public Javabot getBot() {
        return bot;
    }

    public void setBot(final Javabot bot) {
        this.bot = bot;
    }

    /**
     * Returns a list of BotOperation.Message, empty if the operation was not applicable to the message passed. It
     * should never return null.
     */
    public List<Message> handleMessage(final IrcEvent event) {
        return Collections.emptyList();
    }

    public List<Message> handleChannelMessage(final IrcEvent event) {
        return Collections.emptyList();
    }

    public String getName() {
        return getClass().getSimpleName().replaceAll("Operation", "");
    }

    @Override
    public String toString() {
        return getName();
    }

    protected boolean isAdminUser(final IrcEvent event) {
        final IrcUser sender = event.getSender();
        return dao.isAdmin(sender.getNick(), sender.getHost());
    }

    /*
     * Delegates to another method to provide for testability
     */
    protected String formatMessage(String text, String... messages) {
        return formatMessage(text, random.nextInt(messages.length), messages);
    }

    protected String formatMessage(String text, int index, String[] messages) {
        String format = messages[index];
        return String.format(format, text);
    }
}