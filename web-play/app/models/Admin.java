package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "admin")
public class Admin extends Model {
    public String userName;
    public String hostName;
    public String ircName;
    public String addedBy;
    public Date updated;
}