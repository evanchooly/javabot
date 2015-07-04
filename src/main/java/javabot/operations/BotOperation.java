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
     * This method returns where the operation should fall in terms of priority. Lower values represent lower
     * priority. Implementations can probably get by with the default implementation (which returns a priority of 10)
     * but some commands may want to override this.
     * @return the priority of the command
     */
    public int getPriority() { return 10;}

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