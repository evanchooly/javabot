package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "factoids")
public class Factoid extends Model {
    String name;
    String value;
    String userName;
    Date updated;
    Date lastUsed;
    Boolean locked;
}
