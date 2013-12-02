package javabot.model;

import java.io.Serializable;

import com.antwerkz.maven.SPI;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;

@Entity("admins")
@SPI(Persistent.class)
@Indexes({
    @Index(value = "userName", unique = true),
    @Index("ircName, hostName")
} )
public class Admin implements Serializable, Persistent {
  @Id
  private ObjectId id;

  private Boolean botOwner = false;

  private String hostName;

  private String userName;

  private String ircName;

  private String addedBy;

  private DateTime updated = DateTime.now();

  @Override
  public ObjectId getId() {
    return id;
  }

  @Override
  public void setId(ObjectId adminId) {
    id = adminId;
  }

  public Boolean getBotOwner() {
    return botOwner;
  }

  public void setBotOwner(Boolean botOwner) {
    this.botOwner = botOwner;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String adminName) {
    userName = adminName;
  }

  public DateTime getUpdated() {
    return updated;
  }

  public void setUpdated(DateTime date) {
    updated = date;
  }

  public String getAddedBy() {
    return addedBy;
  }

  public void setAddedBy(String addedBy) {
    this.addedBy = addedBy;
  }

  public String getIrcName() {
    return ircName;
  }

  public void setIrcName(String ircName) {
    this.ircName = ircName;
  }

  public String getHostName() {
    return hostName;
  }

  public void setHostName(String hostName) {
    this.hostName = hostName;
  }
}