package javabot.model;

import java.io.Serializable;
import java.util.Date;

import com.antwerkz.maven.SPI;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Indexes;
import org.bson.types.ObjectId;

@Entity("admin")
@SPI(Persistent.class)
@Indexes({
    @Index(value = "userName", unique = true),
    @Index("ircName, hostName")
} )
public class Admin implements Serializable, Persistent {
  @Id
  private ObjectId id;

  private Boolean botOwner;

  private String hostName;

  private String userName;

  private String ircName;

  private String addedBy;

  private Date updated;

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

  public Date getUpdated() {
    return updated;
  }

  public void setUpdated(Date date) {
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