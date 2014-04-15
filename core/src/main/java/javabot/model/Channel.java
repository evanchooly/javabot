package javabot.model;

import java.io.Serializable;

import com.antwerkz.maven.SPI;
import com.fasterxml.jackson.annotation.JsonView;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.PrePersist;
import javabot.Javabot;
import javabot.json.Views.PUBLIC;
import javabot.json.Views.SYSTEM;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity("channels")
@SPI(Persistent.class)
@Indexes({
    @Index(value = "upperName", unique = true, dropDups = true)
})
public class Channel implements Serializable, Persistent {
  private static final Logger log = LoggerFactory.getLogger(Channel.class);

  @Id
  private ObjectId id;

  @JsonView(PUBLIC.class)
  private String name;

  @JsonView(SYSTEM.class)
  private String upperName;

  @JsonView(PUBLIC.class)
  private String key;

  @JsonView(PUBLIC.class)
  private DateTime updated;

  @JsonView(PUBLIC.class)
  private Boolean logged = true;

  public ObjectId getId() {
    return id;
  }

  public void setId(final ObjectId channelId) {
    id = channelId;
  }

  public DateTime getUpdated() {
    return updated;
  }

  public void setUpdated(final DateTime date) {
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