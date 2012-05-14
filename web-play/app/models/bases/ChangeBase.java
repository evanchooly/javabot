package models.bases;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import models.EventType;

@Entity
@Table(name = "changes")
public class ChangeBase extends Model {
  public String message;
  public Date changeDate;
  
}
