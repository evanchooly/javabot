package javabot.model;

import java.io.Serializable;
import javax.inject.Inject;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Transient;
import javabot.Javabot;
import javabot.dao.EventDao;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;

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
  private EventDao eventDao;

  @Id
  private ObjectId id;

  private String requestedBy;

  private DateTime requestedOn;

  private DateTime completed;

  private State state = State.NEW;

  private EventType type;

  protected AdminEvent() {
  }

  public AdminEvent(EventType type, String requestedBy) {
    this.type = type;
    this.requestedBy = requestedBy;
    requestedOn = new DateTime();
  }

  @Override
  public ObjectId getId() {
    return id;
  }

  @Override
  public void setId(ObjectId id) {
    this.id = id;
  }

  public void setCompleted(final DateTime completed) {
    this.completed = completed;
  }

  public DateTime getCompleted() {
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

  public DateTime getRequestedOn() {
    return requestedOn;
  }

  public void setRequestedOn(DateTime requestedOn) {
    this.requestedOn = requestedOn;
  }

  public final void handle(Javabot bot) {
    switch (type) {
      case ADD:
        add(bot);
        break;
      case DELETE:
        delete(bot);
        break;
      case UPDATE:
        update(bot);
        break;
      case RELOAD:
        reload(bot);
        break;
    }
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("AdminEvent{");
    sb.append("id=").append(id);
    sb.append(", requestedOn=").append(requestedOn);
    sb.append(", completed=").append(completed);
    sb.append(", state=").append(state);
    sb.append(", type=").append(type);
    sb.append('}');
    return sb.toString();
  }

  public void add(Javabot bot) {
  }

  public void delete(Javabot bot) {
  }

  public void update(Javabot bot) {
  }

  public void reload(Javabot bot) {
  }
}
