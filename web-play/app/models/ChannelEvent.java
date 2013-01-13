package models;

import com.google.code.morphia.annotations.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class ChannelEvent extends AdminEvent {

    public String channel;
    public String key;

    protected ChannelEvent() {
    }

    public ChannelEvent(String channel, String key, EventType type, String requestedBy) {
        super(type, requestedBy);
        this.key = key;
        this.channel = channel;
    }
}
