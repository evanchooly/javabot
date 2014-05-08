package javabot.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import javax.inject.Inject;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Transient;
import javabot.IrcEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.AdminDao;
import javabot.dao.ApiDao;
import javabot.javadoc.JavadocApi;
import javabot.javadoc.JavadocParser;
import org.bson.types.ObjectId;

@Entity("events")
public class ApiEvent extends AdminEvent {
  private ObjectId apiId;

  public String name;

  public String baseUrl;

  public String downloadUrl;

  @Inject
  @Transient
  private JavadocParser parser;

  @Inject
  @Transient
  private ApiDao apiDao;

  @Inject
  @Transient
  private AdminDao adminDao;

  public ApiEvent() {
  }

  public ApiEvent(String requestedBy, String name, String baseUrl, String downloadUrl) {
    super(EventType.ADD, requestedBy);
    setRequestedBy(requestedBy);
    this.name = name;
    this.baseUrl = baseUrl;
    if(name.equals("JDK")) {
      try {
        this.downloadUrl = new File(System.getProperty("java.home"), "lib/rt.jar").toURI().toURL().toString();
      } catch (MalformedURLException e) {
        throw new IllegalArgumentException(e.getMessage(), e);
      }
    } else {
      this.downloadUrl = downloadUrl;
    }
  }

  public ApiEvent(EventType type, String requestedBy, ObjectId apiId) {
    super(type, requestedBy);
    this.apiId = apiId;
  }

  public ApiEvent(EventType type, String requestedBy, String name) {
    super(type, requestedBy);
    this.name = name;
  }

  public ObjectId getApiId() {
    return apiId;
  }

  public void setApiId(final ObjectId apiId) {
    this.apiId = apiId;
  }

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

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  public void update(Javabot bot) {
    delete(bot);
    add(bot);
  }

  public void delete(Javabot bot) {
    JavadocApi api = apiDao.find(apiId);
    if (api == null) {
      api = apiDao.find(name);
    }
    if (api != null) {
      apiDao.delete(api);
    }
  }

  public void add(final Javabot bot) {
    JavadocApi api = new JavadocApi(name, baseUrl, downloadUrl);
    apiDao.save(api);
    process(bot, api);
  }

  public void reload(final Javabot bot) {
    JavadocApi api = apiDao.find(apiId);
    apiDao.delete(apiId);
    api.setId(null);
    apiDao.save(api);
    process(bot, api);
  }

  private void process(final Javabot bot, final JavadocApi api) {
    final Admin admin = adminDao.getAdmin(getRequestedBy());
    final IrcUser user = new IrcUser(admin.getIrcName(), admin.getIrcName(), admin.getHostName());
    final IrcEvent event = new IrcEvent(admin.getIrcName(), user, null);
    try {
      File file = downloadZip(api.getName() + ".jar", api.getDownloadUrl());
      parser.parse(api, file.getAbsolutePath(), new StringWriter() {
        @Override
        public void write(final String line) {
          event.setMessage(line);
          bot.postMessage(new Message(user, event, line));
        }
      });
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  private File downloadZip(final String fileName, final String zipURL) throws IOException {
    File file = new File("/tmp/" + fileName);
    if (!file.exists()) {
      try (InputStream inputStream = new URL(zipURL).openStream();
           OutputStream fos = new FileOutputStream(file)) {
        byte[] bytes = new byte[8192];
        int read;
        while ((read = inputStream.read(bytes)) != -1) {
          fos.write(bytes, 0, read);
        }
      }
    }
    return file;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("ApiEvent{");
    sb.append("name='").append(name).append('\'');
    sb.append(", state=").append(getState());
    sb.append(", completed=").append(getCompleted());
    sb.append(", type=").append(getType());
    sb.append('}');
    return sb.toString();
  }
}