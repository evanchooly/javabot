package javabot;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import javabot.dao.AdminDao;
import javabot.model.IrcUser;

public class TestJavabot extends Javabot {
  @Inject
  private AdminDao adminDao;
  private List<Message> messages = new ArrayList<>();

  @Override
  protected void createIrcBot() {
    pircBot = new MyPircBot(this, injector) {
      @Override
      public String getNick() {
        return BaseTest.TEST_BOT;
      }
    };
  }

  public List<Message> getMessages() {
    final List<Message> list = messages;
    messages = new ArrayList<>();
    return list;
  }

  @Override
  public void loadConfig() {
    try {
      super.loadConfig();
      IrcUser testUser = BaseTest.TEST_USER;
      if (adminDao.getAdmin(testUser.getUserName()) == null) {
        adminDao.create(testUser.getNick(), testUser.getUserName(), testUser.getHost());
      }
    } catch (Exception e) {
      log.debug(e.getMessage(), e);
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  @Override
  public void connect() {
  }

  @Override
  public void postAction(final Message message) {
    postMessage(message);
  }

  @Override
  public void postMessage(final Message message) {
    logMessage(message);
    log.info(message.toString());
    messages.add(message);
  }

  @Override
  public boolean userIsOnChannel(final String sender, final String channel) {
    return true;
  }
}
