package javabot.model;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import com.antwerkz.maven.SPI;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import org.bson.types.ObjectId;

@Entity("configuration")
/*
@NamedQueries({
  @NamedQuery(name = ConfigDao.GET_CONFIG, query = "select c from Config c")
})
*/
@SPI(Persistent.class)
public class Config implements Serializable, Persistent {
  @Id
  private ObjectId id;
  private String server = "irc.freenode.org";
  private String url;
  private Integer port = 6667;
  private Integer historyLength = 6;
  private String trigger = "~";
  private String nick;
  private String password;
  private Integer schemaVersion;
  private Set<String> operations = new TreeSet<String>();

  public ObjectId getId() {
    return id;
  }

  public void setId(final ObjectId configId) {
    id = configId;
  }

  public String getNick() {
    return nick;
  }

  public void setNick(final String botName) {
    nick = botName;
  }

  public Set<String> getOperations() {
    return operations;
  }

  public void setOperations(final Set<String> list) {
    operations = list;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(final String value) {
    password = value;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(final Integer portNum) {
    port = portNum;
  }

  public String getTrigger() {
    return trigger;
  }

  public void setTrigger(final String nicks) {
    trigger = nicks;
  }

  public String getServer() {
    return server;
  }

  public void setServer(final String ircServer) {
    server = ircServer;
  }

  public Integer getHistoryLength() {
    return historyLength;
  }

  public void setHistoryLength(final Integer historyLength) {
    this.historyLength = historyLength;
  }

  public Integer getSchemaVersion() {
    return schemaVersion == null ? 0 : schemaVersion;
  }

  public void setSchemaVersion(final Integer schemaVersion) {
    this.schemaVersion = schemaVersion;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("Config");
    sb.append("{historyLength=").append(historyLength);
    sb.append(", id=").append(id);
    sb.append(", url='").append(url).append('\'');
    sb.append(", server='").append(server).append('\'');
    sb.append(", port=").append(port);
    sb.append(", trigger='").append(trigger).append('\'');
    sb.append(", nick='").append(nick).append('\'');
    sb.append(", password='").append(password).append('\'');
    sb.append(", schemaVersion=").append(schemaVersion);
    sb.append(", operations=").append(operations);
    sb.append('}');
    return sb.toString();
  }
}
