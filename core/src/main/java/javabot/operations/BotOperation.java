package javabot.operations;

import com.google.inject.Provider;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.AdminDao;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ServiceLoader;

public abstract class BotOperation {
    static Random random = new Random();
    @Inject
    private Javabot bot;
    @Inject
    private AdminDao dao;

    public static List<BotOperation> list() {
        final ServiceLoader<BotOperation> loader = ServiceLoader.load(BotOperation.class);
        final List<BotOperation> list = new ArrayList<>();
        for (final BotOperation operation : loader) {
            list.add(operation);
        }
        return list;
    }

    public Javabot getBot() {
        return bot;
    }

    /**
     * @return true if the message has been handled
     */
    public boolean handleMessage(final Message event) {
        return false;
    }

    public boolean handleChannelMessage(final Message event) {
        return false;
    }

    public String getName() {
        return getClass().getSimpleName().replaceAll("Operation", "");
    }

    @Override
    public String toString() {
        return getName();
    }

    protected boolean isAdminUser(final User user) {
        return dao.isAdmin(user);
    }

    protected String formatMessage(String text, String... messages) {
        return formatMessage(text, random.nextInt(messages.length), messages);
    }

    protected String formatMessage(String text, int index, String[] messages) {
        String format = messages[index];
        return String.format(format, text);
    }
}