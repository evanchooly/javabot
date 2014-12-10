package javabot.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(value = "changes", noClassnameStored = true)
public class Change implements Serializable, Persistent {

    @Id
    private ObjectId id;
    private String message;
    @Indexed(name = "changed")
    private LocalDateTime changeDate;

    public Change() {
    }

    public Change(final String message) {
        this.message = message;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String value) {
        message = value;
    }

    public LocalDateTime getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(LocalDateTime DateTime) {
        changeDate = DateTime;
    }
}
