package javabot.operations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

import javabot.IrcEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.AdminDao;
import org.schwering.irc.lib.IRCUser;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BotOperation {
    private Javabot bot;
    @Autowired
    private AdminDao dao;
    private transient boolean enabled = false;
    private final OperationComparator operationComparator = new OperationComparator(this);

    public Javabot getBot() {
        return bot;
    }

    public void setBot(final Javabot bot) {
        this.bot = bot;
    }

    public boolean isStandardOperation() {
        return false;
    }

    public final int getPriority() {
        return isStandardOperation() ? Integer.MAX_VALUE : 0;
    }
    
    /**
     * Returns a list of BotOperation.Message, empty if the operation was not applicable to the message passed. It
     * should never return null.
     *
     * @param event
     *
     * @return
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
        return String.format("%s [%s]", getName(), isStandardOperation() ? "standard" : isEnabled());
    }

    public static List<BotOperation> list() {
        final ServiceLoader<BotOperation> loader = ServiceLoader.load(BotOperation.class);
        final List<BotOperation> list = new ArrayList<BotOperation>();
        for (final BotOperation operation : loader) {
            list.add(operation);
        }
        return list;
    }

    protected boolean isAdminUser(final IrcEvent event) {
        final IRCUser sender = event.getSender();
        return dao.isAdmin(sender.getNick(), sender.getHost());
    }

    public boolean isEnabled() {
        return isStandardOperation() || enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}