package models.bases;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import models.EventType;

@Entity
@Table(name = "karma")
public class KarmaBase extends Model {
  public String name;
  public Integer value;
  public String userName;
  public Date updated;
  
}
