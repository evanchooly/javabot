package models.bases;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import models.EventType;

@Entity
@Table(name = "configuration")
public class ConfigBase extends Model {
  public String server;
  public Integer port;
  public Integer historyLength;
  public String trigger;
  public String nick;
  public String password;
  public Integer schemaVersion;
  public String> operations;
  
}
