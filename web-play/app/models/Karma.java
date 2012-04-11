package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="karma")
public class Karma extends Model {
    String name;
    Integer value = 0;
    String userName;
    Date updated;
}
