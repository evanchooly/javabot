package models;

import java.util.Date;
import com.google.code.morphia.annotations.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
("admin")
public class Admin extends Model {
  public Boolean botOwner;
  public String userName;
  public String hostName;
  public String ircName;
  public String addedBy;
  public Date updated;

  public Admin(String ircName, String hostName, String twitter, String addedBy) {
    this.ircName = ircName;
    this.hostName = hostName;
    userName = twitter;
    this.addedBy = addedBy;
    updated = new Date();
    botOwner = false;
  }
}