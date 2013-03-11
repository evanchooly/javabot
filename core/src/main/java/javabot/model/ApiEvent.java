package javabot.model;

import java.io.StringWriter;
import javax.inject.Inject;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Transient;
import javabot.IrcEvent;
import javabot.IrcUser;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.AdminDao;
import javabot.dao.ApiDao;
import javabot.javadoc.Api;
import javabot.javadoc.JavadocParser;

@Entity("events")
public class ApiEvent extends AdminEvent {
  public String name;
  public String packages;
  public String baseUrl;
  public String file;
  @Inject
  @Transient
  private JavadocParser parser;
  @Inject
  @Transient
  private ApiDao apiDao;
  @Inject
  @Transient
  private AdminDao adminDao;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
  }

  public String getPackages() {
    return packages;
  }

  public void setPackages(String packages) {
    this.packages = packages;
  }

  public void update(Javabot bot) {
    delete(bot);
    add(bot);
  }

  public void delete(Javabot bot) {
    Api api = apiDao.find(name);
    if(api != null) {
      apiDao.delete(api);
    }
  }

  public void add(final Javabot bot) {
    Api api = new Api(name, baseUrl, packages);
    apiDao.save(api);

    final Admin admin = adminDao.getAdmin(getRequestedBy());
    final IrcUser user = new IrcUser(admin.getIrcName(), admin.getIrcName(), admin.getHostName());
    final IrcEvent event = new IrcEvent(admin.getIrcName(), user, null);

    parser.parse(api, file, new StringWriter() {
      @Override
      public void write(final String line) {
        event.setMessage(line);
        bot.postMessage(new Message(user, event, line));
      }
    });
  }
}