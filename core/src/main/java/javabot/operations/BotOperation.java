package javabot.operations;

import java.util.*;
import javax.inject.Inject;

import javabot.IrcEvent;
import javabot.IrcUser;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.AdminDao;

public abstract class BotOperation {
  private Javabot bot;
  @Inject
  private AdminDao dao;

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

  public static List<BotOperation> list() {
    final ServiceLoader<BotOperation> loader = ServiceLoader.load(BotOperation.class);
    final List<BotOperation> list = new ArrayList<BotOperation>();
    for (final BotOperation operation : loader) {
      list.add(operation);
    }
    return list;
  }

  protected boolean isAdminUser(final IrcEvent event) {
    final IrcUser sender = event.getSender();
    return dao.isAdmin(sender.getNick(), sender.getHost());
  }

    protected String formatMessage(String nick, String... messages) {
        Random random=new Random();
        String format=messages[random.nextInt(messages.length)];
        return String.format(format, nick);
    }
}