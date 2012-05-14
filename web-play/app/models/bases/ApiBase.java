package models.bases;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import models.EventType;

@Entity
@Table(name = "apis")
public class ApiBase extends Model {
  public Logger log;
  public String name;
  public String baseUrl;
  public String packages;
  public Clazz> classes;
  public String> JDK_JARS;
  
}
