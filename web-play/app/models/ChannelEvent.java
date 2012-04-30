package models;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "events")
public class ChannelEvent extends AdminEvent {
    enum Type {
        ADD,
        DELETE,
        UPDATE
    }

    public String channel;
    public String key;
    private String requestedBy;
    private Date requestedOn;
    @Enumerated(EnumType.STRING)
    public Type type;

    protected ChannelEvent() {
    }

    public ChannelEvent(String channel, String key, Type type, String requestedBy) {
        super(requestedBy);
        this.type = type;
        this.key = key;
        this.channel = channel;
    }
}
