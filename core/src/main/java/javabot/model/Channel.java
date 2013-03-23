package javabot.model;

import java.io.Serializable;
import java.util.Date;

import com.antwerkz.maven.SPI;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Indexes;
import com.google.code.morphia.annotations.PrePersist;
import javabot.Javabot;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity("channel")
@SPI(Persistent.class)
@Indexes({
    @Index(value = "upperName", unique = true, dropDups = true)
})
public class Channel implements Serializable, Persistent {
  private static final Logger log = LoggerFactory.getLogger(Channel.class);

  @Id
  private ObjectId id;

  private String name;

  private String upperName;

  private String key;

  private Date updated;

  private Boolean logged = true;

  public ObjectId getId() {
    return id;
  }

  public void setId(final ObjectId channelId) {
    id = channelId;
  }

  public Date getUpdated() {
    return updated;
  }

  public void setUpdated(final Date date) {
    updated = date;
  }

  public String getKey() {
    return key;
  }

  public void setKey(final String key) {
    this.key = key;
  }

  public Boolean getLogged() {
    return logged == null ? Boolean.FALSE : logged;
  }

  public void setLogged(final Boolean isLogged) {
    logged = isLogged;
  }

  public String getName() {
    return name;
  }

  public void setName(final String chanName) {
    name = chanName;
  }

  @Override
  public String toString() {
    return "Channel{" +
        "id=" + id +
        ", logged=" + logged +
        ", name='" + name + '\'' +
        ", updated=" + updated +
        '}';
  }

  public String getUpperName() {
    return upperName;
  }

  public void setUpperName(final String upperName) {
    this.upperName = upperName;
  }

  @PrePersist
  public void uppers() {
    upperName = name.toUpperCase();
  }
  public void join(final Javabot bot) {
    bot.join(name, key);
  }

  public void leave(final Javabot bot, final String reason) {
    bot.leave(name, reason);
  }
}