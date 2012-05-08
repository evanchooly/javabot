package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "factoids")
public class Factoid extends Model {
    public String name;
    public String value;
    public String userName;
    public Date updated;
    public Date lastUsed;
    public Boolean locked;
}
