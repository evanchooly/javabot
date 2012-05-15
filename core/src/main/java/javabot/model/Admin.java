package javabot.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.antwerkz.maven.SPI;
import javabot.dao.AdminDao;

@Entity
@Table(name = "admin")
@NamedQueries({
  @NamedQuery(name = AdminDao.FIND, query = "select a from Admin a where a.userName = :username "),
  @NamedQuery(name = AdminDao.FIND_WITH_HOST, query = "select a from Admin a where a.userName = :username "
    + "and a.hostName = :hostName"),
  @NamedQuery(name = AdminDao.FIND_ALL, query = "select a from Admin a order by a.userName")
})
@SPI(Persistent.class)
public class Admin implements Serializable, Persistent {
  private Long id;
  private Boolean botOwner;
  private String hostName;
  private String userName;
  private String ircName;
  private String addedBy;
  private Date updated;

  @Id
  @Override
  @GeneratedValue
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long adminId) {
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