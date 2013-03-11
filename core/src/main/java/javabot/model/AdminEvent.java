package javabot.model;

import java.io.Serializable;
import java.util.Date;
import javax.inject.Inject;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Transient;
import javabot.Javabot;
import javabot.dao.EventDao;
import org.bson.types.ObjectId;

@Entity("events")
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
  private Date requestedOn;
  private State state = State.NEW;
  private EventType type;

  protected AdminEvent() {
  }

  public AdminEvent(EventType type, String requestedBy) {
    this.type = type;
    this.requestedBy = requestedBy;
    requestedOn = new Date();
  }

  @Override
  public ObjectId getId() {
    return id;
  }

  @Override
  public void setId(ObjectId id) {
    this.id = id;
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

  public Date getRequestedOn() {
    return requestedOn;
  }

  public void setRequestedOn(Date requestedOn) {
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
    }
  }

  public void add(Javabot bot) {
  }

  public void delete(Javabot bot) {
  }

  public void update(Javabot bot) {
  }
}
