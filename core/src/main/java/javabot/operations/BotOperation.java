package javabot.operations;

import javabot.Javabot;
import javabot.Message;
import javabot.dao.AdminDao;
import org.pircbotx.User;

import javax.inject.Inject;
import java.util.Random;

public abstract class BotOperation {
    static Random random = new Random();
    @Inject
    private Javabot bot;
    @Inject
    private AdminDao dao;

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