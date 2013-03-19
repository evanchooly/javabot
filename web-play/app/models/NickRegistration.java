package models;

import com.google.code.morphia.annotations.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity ("registrations")
public class NickRegistration extends Model {
  public String url;
  public String nick;
  public String host;
  public String twitterName;

}
