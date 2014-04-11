package utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.google.inject.Inject;
import javabot.model.Admin;
import javabot.model.Config;
import javabot.model.EventType;
import javabot.model.OperationEvent;
import play.api.Play;
import play.api.libs.Files;

public class ConfigDao extends javabot.dao.ConfigDao {
  @Inject
  private play.api.Application application;

  public void save(Admin admin, Config info) {
    String url = info.getUrl();
    while (url.endsWith("/")) {
      url = url.substring(0, url.length() - 1);
    }
    Config old = get();
    old.setServer(info.getServer());
    old.setHistoryLength(info.getHistoryLength());
    old.setNick(info.getNick());
    old.setPassword(info.getPassword());
    old.setPort(info.getPort());
    old.setTrigger(info.getTrigger());
    old.setUrl(url);
    save(old);
  }

  public List<String> operations() {
    File file = Play.getFile("conf/operations.list", application);
    return Arrays.asList(file.exists() ? Files.readFile(file).split("\n") : new String[0]);
  }

  public void updateOperations(Admin admin, List<String> old, List<String> updated) {
    for (String operation : operations()) {
      if (old.contains(operation) && !updated.contains(operation)) {
        save(new OperationEvent(EventType.DELETE, operation, admin.getUserName()));
      } else if (!old.contains(operation) && updated.contains(operation)) {
        save(new OperationEvent(EventType.ADD, operation, admin.getUserName()));
      }
    }
  }
}