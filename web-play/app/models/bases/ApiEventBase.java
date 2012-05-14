package models.bases;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import models.EventType;

@Entity
public class ApiEventBase extends Model {
  public String name;
  public String packages;
  public String baseUrl;
  public String file;
  
}
