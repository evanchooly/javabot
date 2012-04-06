package models;

import play.db.jpa.Model;

import javax.persistence.Table;
import java.util.Date;

@Table(name = "admin")
public class Admin extends Model {
    String userName;
    String hostName;
    Date updated;
}