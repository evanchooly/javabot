package javabot;

import java.util.EnumSet;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

import com.google.inject.Injector;
import com.jayway.awaitility.Awaitility;
import com.jayway.awaitility.Duration;
import javabot.dao.AdminDao;
import javabot.dao.ChannelDao;
import javabot.dao.EventDao;
import javabot.model.AdminEvent;
import javabot.model.AdminEvent.State;
import javabot.model.Channel;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Guice;

@Guice(modules = {JavabotModule.class})
@SuppressWarnings({"StaticNonFinalField"})
public class BaseTest {
  protected static final String TEST_BOT = "jbtestbot";

  public static final IrcUser TEST_USER = new IrcUser("jbtestuser", "jbtestuser", "localhost");

  public EnumSet<State> done = EnumSet.of(State.COMPLETED, State.FAILED);

  @Inject
  protected EventDao eventDao;

  @Inject
  private AdminDao adminDao;

  @Inject
  private ChannelDao channelDao;

  @Inject
  private Injector injector;

  public final String ok;

  protected TestJavabot bot;

  public BaseTest() {
    Javabot.setPropertiesFile("test-javabot.properties");
    final String nick = BaseTest.TEST_USER.getNick();
    ok = "OK, " + nick.substring(0, Math.min(nick.length(), 16)) + ".";
  }

  public void drainMessages() {
    Awaitility.await("Draining Messages")
        .atMost(1, TimeUnit.HOURS)
        .pollInterval(5, TimeUnit.SECONDS)
        .until(new Callable<Boolean>() {
          @Override
          public Boolean call() throws Exception {
            return getJavabot().getMessages().isEmpty() ;
          }
        });
  }

  protected TestJavabot createBot() {
    if (bot == null) {
      Javabot.validateProperties();
      bot = injector.getInstance(TestJavabot.class);
      bot.start();
    }
    return bot;
  }

  public final TestJavabot getJavabot() {
    if (bot == null) {
      createBot();
    }
    Channel channel = channelDao.get(getJavabotChannel());
    if (channel == null) {
      channel = new Channel();
      channel.setName(getJavabotChannel());
      channel.setLogged(true);
      channelDao.save(channel);
    }
    return bot;
  }

  protected String getJavabotChannel() {
    return "#jbunittest";
  }

  @SuppressWarnings({"EmptyCatchBlock"})
  protected static void sleep(final int milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException exception) {
    }
  }

  @AfterSuite
  public void shutdown() throws InterruptedException {
    if (bot != null) {
      bot.shutdown();
    }
  }

  protected void waitForEvent(final AdminEvent event, final String alias, final Duration timeout) {
    Awaitility.await(alias)
        .atMost(timeout)
        .pollInterval(5, TimeUnit.SECONDS)
        .until(new Callable<Boolean>() {
          @Override
          public Boolean call() throws Exception {
            return done.contains(eventDao.find(event.getId()).getState());
          }
        });
  }
}