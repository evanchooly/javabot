package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "events")
public class AdminEvent extends Model {
    public String requestedBy;
    public Date requestedOn;
    public Boolean processed;

    protected AdminEvent() {
    }

    public AdminEvent(String requestedBy, Date requestedOn) {
        this.requestedBy = requestedBy;
        this.requestedOn = requestedOn;
        processed = false;
    }
}
