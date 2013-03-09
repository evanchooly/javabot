package javabot.model;

import java.io.Serializable;
import java.util.Date;
import javax.inject.Inject;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import javabot.Javabot;
import javabot.dao.EventDao;
import org.bson.types.ObjectId;

@Entity("events")
public class AdminEvent implements Serializable, Persistent {
  @Inject
  private EventDao eventDao;
  @Id
  private ObjectId id;
  private String requestedBy;
  private Date requestedOn;
  private Boolean processed;
  private EventType type;

  protected AdminEvent() {
  }

  public AdminEvent(EventType type, String requestedBy) {
    this.type = type;
    this.requestedBy = requestedBy;
    requestedOn = new Date();
    processed = false;
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

  public Boolean getProcessed() {
    return processed;
  }

  public void setProcessed(Boolean processed) {
    this.processed = processed;
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
    setProcessed(true);
    eventDao.save(this);
  }

  public void add(Javabot bot) {
  }

  public void delete(Javabot bot) {
  }

  public void update(Javabot bot) {
  }
}
