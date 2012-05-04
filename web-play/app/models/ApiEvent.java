package models;

import javax.persistence.Entity;

@Entity
public class ApiEvent extends AdminEvent {
    public String name;

    public ApiEvent(String name, EventType type, String requestedBy) {
        super(type, requestedBy);
        this.name = name;
    }
}
