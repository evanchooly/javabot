package models.bases;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import models.EventType;

@Entity
@Table(name = "events")
public class AdminEventBase extends Model {
  public String requestedBy;
  public Date requestedOn;
  public Boolean processed;
  public EventType type;
  
}
