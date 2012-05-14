package models.bases;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import models.EventType;

@Entity
@Table(name = "classes")
public class ClazzBase extends Model {
  public Logger log;
  public Api api;
  public String packageName;
  public String className;
  public Clazz superClass;
  public Method> methods;
  public Field> fields;
  
}
