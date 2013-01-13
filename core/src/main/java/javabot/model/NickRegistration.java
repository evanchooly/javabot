package javabot.model;

import java.io.Serializable;
import java.util.UUID;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import javabot.IrcUser;

@Entity ("registrations")
public class NickRegistration implements Serializable, Persistent {
    @Id
  private Long id;
  private String url;
  private String nick;
  private String host;
  private String twitterName;

  public NickRegistration() {
  }

  public NickRegistration(IrcUser sender, String twitterName) {
    this.twitterName = twitterName;
    url = UUID.randomUUID().toString();
    nick = sender.getNick();
    host = sender.getHost();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getNick() {
    return nick;
  }

  public void setNick(String nick) {
    this.nick = nick;
  }

  public String getTwitterName() {
    return twitterName;
  }

  public void setTwitterName(String twitterName) {
    this.twitterName = twitterName;
  }
}
