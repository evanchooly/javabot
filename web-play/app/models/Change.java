package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "changes")
public class Change extends Model {
    String message;
    Date changeDate;
}
