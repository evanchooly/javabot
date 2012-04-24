package models;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class ChannelEvent extends AdminEvent {
    public String channel;
    public boolean added;

    protected ChannelEvent() {
    }

    public ChannelEvent(String channel, boolean added, String requestedBy, Date requestedOn) {
        super(requestedBy, requestedOn);
        this.added = added;
        this.channel = channel;
    }
}
