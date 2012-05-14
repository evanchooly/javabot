package models.bases;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import models.EventType;

@Entity
@Table(name = "shun")
public class ShunBase extends Model {
  public String nick;
  public Date expiry;
  
}
