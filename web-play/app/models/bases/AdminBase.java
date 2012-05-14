package models.bases;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import models.EventType;

@Entity
@Table(name = "admin")
public class AdminBase extends Model {
  public String hostName;
  public String userName;
  public String ircName;
  public String addedBy;
  public Date updated;
  
}
