package utils;

import java.util.List;
import javax.inject.Inject;

import javabot.dao.ChannelDao;
import javabot.dao.FactoidDao;
import javabot.model.Admin;
import javabot.model.Logs;
import org.joda.time.DateTime;
import play.mvc.Http;

public class Context {
  @Inject
  private FactoidDao factoidDao;

  @Inject
  private ChannelDao channelDao;

  @Inject
  private AdminDao adminDao;

  @Inject
  private LogsDao logsDao;

  public List<Logs> logs;

  public String channel;

  public Long factoidCount() {
    return factoidDao.count();
  }

  public Boolean showAll() {
    String userName = Http.Context.current().session().get("userName");
    if (userName != null) {
      Admin admin = adminDao.getAdmin(userName);
      return admin != null && admin.getBotOwner();
    }
    return false;
  }

  public List<javabot.model.Channel> channels() {
    return channelDao.findLogged(showAll());
  }

  public List<Logs> getLogs() {
    return logs;
  }

  public void logChannel(String name, DateTime date) {
    channel = name;
    logs = logsDao.findByChannel(channel, date, showAll());
  }
}

