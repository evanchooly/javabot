package models.bases;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import models.EventType;

@Entity
public class ChannelEventBase extends Model {
  public String channel;
  public String key;
  public Boolean logged;
  
}
