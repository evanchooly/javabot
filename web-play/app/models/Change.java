package models;

import play.db.jpa.Model;

import com.google.code.morphia.annotations.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
("changes")
public class Change extends Model {
    String message;
    Date changeDate;
}
