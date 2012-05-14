package models.bases;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import models.EventType;

@Entity
@Table(name = "fields")
public class FieldBase extends Model {
  public Clazz clazz;
  public String name;
  public String type;
  
}
