package javabot;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import com.google.inject.Injector;
import javabot.dao.NickServDao;
import javabot.model.Channel;
import javabot.model.IrcUser;
import javabot.model.Logs;
import javabot.operations.throttle.Throttler;
import org.jibble.pircbot.PircBot;

public class MyPircBot extends PircBot {
  @Inject
  private Throttler throttler;

  @Inject
  private NickServDao nickServDao;

  private final List<String> nickServ = new ArrayList<>();

  private final Javabot javabot;

  public MyPircBot(final Javabot javabot, final Injector injector) {
    this.javabot = javabot;
    setVersion(javabot.loadVersion());
    setName(javabot.config.getNick());
    setLogin(javabot.config.getNick());
    injector.injectMembers(this);
  }

  public void log(final String string) {
    if (Javabot.log.isInfoEnabled()) {
      Javabot.log.info(string);
    }
  }

  public String getNick() {
    return javabot.nick;
  }

  @Override
  public void onMessage(final String channel, final String sender, final String login, final String hostname,
      final String message) {
    javabot.executors.execute(
        () -> javabot.processMessage(new IrcEvent(channel, javabot.getUser(sender, login, hostname), message)));
  }

  @Override
  public void onJoin(final String channel, final String sender, final String login, final String hostname) {
    javabot.logsDao.logMessage(Logs.Type.JOIN, sender, channel, ":" + hostname + " joined the channel");
  }

  @Override
  public void onPart(final String channel, final String sender, final String login, final String hostname) {
    javabot.logsDao.logMessage(Logs.Type.PART, sender, channel, ":" + hostname + " parted the channel");
  }

  @Override
  public void onQuit(final String channel, final String sender, final String login, final String hostname) {
    javabot.logsDao.logMessage(Logs.Type.QUIT, sender, channel, "quit");
  }

  @Override
  public void onInvite(final String targetNick, final String sourceNick, final String sourceLogin,
      final String sourceHostname, final String channelName) {
    final Channel channel = javabot.channelDao.get(channelName);
    if (channel != null) {
      channel.join(javabot);
    }
  }

  @Override
  public void onDisconnect() {
    System.out.println("MyPircBot.onDisconnect");
    if (!javabot.executors.isShutdown()) {
      try {
        System.out.println("trying to reconnect");
        javabot.setReconnecting(true);
        javabot.connect();
      } catch (Exception e) {
        Javabot.log.error(e.getMessage(), e);
        throw new RuntimeException(e.getMessage(), e);
      }
    }
  }

  @Override
  protected void onNotice(final String sourceNick, final String sourceLogin, final String sourceHostname,
      final String target, final String notice) {
    super.onNotice(sourceNick, sourceLogin, sourceHostname, target, notice);
    if (sourceNick.equalsIgnoreCase("NickServ")) {
      String message = notice.replace("\u0002", "");
      synchronized (nickServ) {
        if (message.equals("*** End of Info ***")) {
          try {
            nickServDao.process(nickServ);
          } catch (Exception e) {
            e.printStackTrace();  
          }
          nickServ.clear();
        } else {
          System.out.println(message);
          if(message.startsWith("Information on ") || !nickServ.isEmpty()) {
            nickServ.add(message);
          }
        }
      }
    }
  }

  @Override
  public void onPrivateMessage(final String sender, final String login, final String hostname, final String message) {
    if (javabot.adminDao.isAdmin(sender, hostname) || javabot.isOnSameChannelAs(sender)) {
      javabot.executors.execute(() -> {
        javabot.logsDao.logMessage(Logs.Type.MESSAGE, sender, sender, message);
        IrcUser user = javabot.getUser(sender, login, hostname);
        if (!throttler.isThrottled(user, this)) {
          for (final Message response : javabot.getResponses(sender, user, message)) {
            response.send(javabot);
          }
        }
      });
    }
  }

  @Override
  public void onAction(final String sender, final String login, final String hostname, final String target,
      final String action) {
    javabot.logsDao.logMessage(Logs.Type.ACTION, sender, target, action);
  }

  @Override
  public void onKick(final String channel, final String kickerNick, final String kickerLogin,
      final String kickerHostname, final String recipientNick, final String reason) {
    javabot.logsDao
        .logMessage(Logs.Type.KICK, kickerNick, channel, " kicked " + recipientNick + " (" + reason + ")");
  }
}
