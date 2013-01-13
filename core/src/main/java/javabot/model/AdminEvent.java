package javabot.model;

import java.io.Serializable;
import java.util.Date;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import javabot.Javabot;
import javabot.dao.EventDao;

@Entity("events")
public class AdminEvent implements Serializable, Persistent {
    @Id
    private Long id;
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
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
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
        bot.getApplicationContext().getBean(EventDao.class).save(this);
    }

    public void add(Javabot bot) {
    }

    public void delete(Javabot bot) {
    }

    public void update(Javabot bot) {
    }
}
