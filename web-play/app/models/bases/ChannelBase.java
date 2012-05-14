package models.bases;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import models.EventType;

@Entity
@Table(name = "channel")
public class ChannelBase extends Model {
  public Logger log;
  public String name;
  public String key;
  public Date updated;
  public Boolean logged;
  
}
