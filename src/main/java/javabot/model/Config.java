package javabot.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Entity("configuration")
public class Config implements Serializable, Persistent {
  @Id
  private ObjectId id;

  private String server = "irc.freenode.org";

  private String url;

  private Integer port = 6667;

  private Integer historyLength = 12;

  private String trigger = "~";

  private String nick;

  private String password;

  private Integer schemaVersion;

  private List<String> operations = new ArrayList<>();

  private Integer throttleThreshold = 5;

  private Integer minimumNickServAge = 14;

  public Config() {
  }

  public Config(final ObjectId id, final String server, final String url, final Integer port,
      final Integer historyLength, final String trigger, final String nick, final String password,
      final List<String> operations) {
    this.id = id;
    this.historyLength = historyLength;
    this.nick = nick;
    this.operations = operations;
    this.password = password;
    this.port = port;
    this.server = server;
    this.trigger = trigger;
    this.url = url;
  }

  public ObjectId getId() {
    return id;
  }

  public void setId(final ObjectId configId) {
    id = configId;
  }

  public Integer getMinimumNickServAge() {
    return minimumNickServAge;
  }

  public void setMinimumNickServAge(final Integer minimumNickServAge) {
    this.minimumNickServAge = minimumNickServAge;
  }

  public String getNick() {
    return nick;
  }

  public void setNick(final String botName) {
    nick = botName;
  }

  public List<String> getOperations() {
    return operations;
  }

  public void setOperations(final List<String> list) {
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

  public Integer getThrottleThreshold() {
    return throttleThreshold;
  }

  public void setThrottleThreshold(final Integer throttleThreshold) {
    this.throttleThreshold = throttleThreshold;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public String toString() {
    return "Config{" +
        "server='" + server + '\'' +
        ", url='" + url + '\'' +
        ", port=" + port +
        ", historyLength=" + historyLength +
        ", trigger='" + trigger + '\'' +
        ", nick='" + nick + '\'' +
        ", schemaVersion=" + schemaVersion +
        ", throttleThreshold=" + throttleThreshold +
        ", mininumNickServAge=" + minimumNickServAge +
        ", operations=" + operations +
        '}';
  }
}
