package models;

import play.db.jpa.Model;

import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import java.util.Date;

@MappedSuperclass
@Table(name = "events")
public class AdminEvent extends Model {
    public String requestedBy;
    public Date requestedOn;
    public Boolean processed;

    protected AdminEvent() {
    }

    public AdminEvent(String requestedBy) {
        this.requestedBy = requestedBy;
        requestedOn = new Date();
        processed = false;
    }
}
