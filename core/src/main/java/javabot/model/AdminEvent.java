package javabot.model;

import javabot.Javabot;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Transient;

import javax.inject.Inject;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity("events")
@Indexes({
             @Index("state, requestedOn")
         })
public class AdminEvent implements Serializable, Persistent {
    public enum State {
        NEW,
        PROCESSING,
        COMPLETED,
        FAILED
    }

    @Inject
    @Transient
    private Javabot bot;

    @Id
    private ObjectId id;

    private String requestedBy;

    private LocalDateTime requestedOn;

    @Indexed(expireAfterSeconds = 60 * 60 * 24)
    private LocalDateTime completed;

    private State state = State.NEW;

    private EventType type;

    protected AdminEvent() {
    }

    public AdminEvent(EventType type, String requestedBy) {
        this.type = type;
        this.requestedBy = requestedBy;
        requestedOn = LocalDateTime.now();
    }

    public Javabot getBot() {
        return bot;
    }

    @Override
    public ObjectId getId() {
        return id;
    }

    @Override
    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setCompleted(final LocalDateTime completed) {
        this.completed = completed;
    }

    public LocalDateTime getCompleted() {
        return completed;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public State getState() {
        return state;
    }

    public void setState(final State state) {
        this.state = state;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public LocalDateTime getRequestedOn() {
        return requestedOn;
    }

    public void setRequestedOn(LocalDateTime requestedOn) {
        this.requestedOn = requestedOn;
    }

    public final void handle() {
        switch (type) {
            case ADD:
                add();
                break;
            case DELETE:
                delete();
                break;
            case UPDATE:
                update();
                break;
            case RELOAD:
                reload();
                break;
        }
    }

    @Override
    public String toString() {
        return String.format("AdminEvent{id=%s, requestedOn=%s, completed=%s, state=%s, type=%s}", id, requestedOn, completed, state, type);
    }

    public void add() {
    }

    public void delete() {
    }

    public void update() {
    }

    public void reload() {
    }
}
