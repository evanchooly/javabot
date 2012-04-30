package javabot.model;

import javabot.Javabot;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public class AdminEvent implements Serializable, Persistent {
    private Long id;
    private String requestedBy;
    private Date requestedOn;
    private Boolean processed;

    protected AdminEvent() {
    }

    public AdminEvent(String requestedBy) {
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

    public void handle(Javabot bot) {
        System.out.println("AdminEvent.handle");
    }
}
