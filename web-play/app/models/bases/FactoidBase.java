package models.bases;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import models.EventType;

@Entity
@Table(name = "factoids")
public class FactoidBase extends Model {
  public Logger log;
  public String name;
  public String value;
  public String userName;
  public Date updated;
  public Date lastUsed;
  public Boolean locked;
  
}
