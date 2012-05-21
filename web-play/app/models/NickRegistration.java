package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name = "registrations")
public class NickRegistration extends Model {
  public String url;
  public String nick;
  public String host;
  public String twitterName;

}
