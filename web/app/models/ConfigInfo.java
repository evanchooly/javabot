package models;

public class ConfigInfo {
  private String server;

  private String url;

  private Integer port;

  private Integer historyLength;

  private String trigger;

  private String nick;

  private String password;

  public String getServer() {
    return server;
  }

  public void setServer(final String server) {
    this.server = server;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(final String url) {
    this.url = url;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(final Integer port) {
    this.port = port;
  }

  public Integer getHistoryLength() {
    return historyLength;
  }

  public void setHistoryLength(final Integer historyLength) {
    this.historyLength = historyLength;
  }

  public String getTrigger() {
    return trigger;
  }

  public void setTrigger(final String trigger) {
    this.trigger = trigger;
  }

  public String getNick() {
    return nick;
  }

  public void setNick(final String nick) {
    this.nick = nick;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }
}
