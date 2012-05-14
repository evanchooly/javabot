package models.bases;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import models.EventType;

@Entity
@Table(name = "methods")
public class MethodBase extends Model {
  public Clazz clazz;
  public String methodName;
  public String longSignatureTypes;
  public String shortSignatureTypes;
  public String longSignatureStripped;
  public String shortSignatureStripped;
  public Integer paramCount;
  
}
