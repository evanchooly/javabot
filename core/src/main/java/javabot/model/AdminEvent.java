package javabot.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javabot.Javabot;
import javabot.dao.EventDao;

@Entity
@Table(name = "events")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(length = 255)
@NamedQueries({
  @NamedQuery(name = EventDao.FIND_ALL, query = "select a from javabot.model.AdminEvent a where" +
    " a.processed = false order by a.requestedOn ASC")
})
public class AdminEvent implements Serializable, Persistent {
  private Long id;
  private String requestedBy;
  private Date requestedOn;
  private Boolean processed;
  @Enumerated(EnumType.STRING)
  public EventType type;

  protected AdminEvent() {
  }

  public AdminEvent(EventType type, String requestedBy) {
    this.type = type;
    this.requestedBy = requestedBy;
    requestedOn = new Date();
    processed = false;
  }

  @Id
  @GeneratedValue
  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  @Enumerated(EnumType.STRING)
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

  @Temporal(TemporalType.TIMESTAMP)
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
    bot.getApplicationContext().getBean(EventDao.class).save(this);
  }

  public void add(Javabot bot) {
  }

  public void delete(Javabot bot) {
  }

  public void update(Javabot bot) {
  }
}
