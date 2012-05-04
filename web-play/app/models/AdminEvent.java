package models;

import play.db.jpa.Model;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "events")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(length = 255)
public class AdminEvent extends Model {
    public String requestedBy;
    public Date requestedOn;
    public Boolean processed;
    @Enumerated(EnumType.STRING)
    public EventType type;

    public AdminEvent() {
    }

    protected AdminEvent(EventType type, String requestedBy) {
        requestedOn = new Date();
        processed = false;
        this.type = type;
        this.requestedBy = requestedBy;
    }
}
