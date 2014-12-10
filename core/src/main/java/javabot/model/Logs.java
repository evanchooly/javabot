package javabot.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.PrePersist;

import java.time.LocalDateTime;

@Entity(value = "logs", noClassnameStored = true)
@Indexes({
             @Index(value = "channel, upperNick, updated", name = "seen"),
         })
public class Logs implements Persistent {
    @Id
    private ObjectId id;

    private String nick;

    private String upperNick;

    private String channel;

    private String message;

    private LocalDateTime updated;

    public enum Type {
        ACTION,
        BAN,
        DISCONNECTED,
        ERROR,
        INVITE,
        JOIN,
        PART,
        KICK,
        MESSAGE,
        QUIT,
        REGISTERED,
        TOPIC,
        NICK,
    }

    private Type type;

    public ObjectId getId() {
        return id;
    }

    public void setId(final ObjectId logsId) {
        id = logsId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(final String user) {
        nick = user;
    }

    public String getUpperNick() {
        return upperNick;
    }

    public void setUpperNick(final String upperNick) {
        this.upperNick = upperNick;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(final String chanName) {
        channel = chanName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String logMessage) {
        message = logMessage;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(final LocalDateTime date) {
        updated = date;
    }

    public Type getType() {
        return type;
    }

    public void setType(final Type value) {
        type = value;
    }

    public boolean isAction() {
        return message != null && Type.ACTION == getType();
    }

    public boolean isKick() {
        return message != null && Type.KICK == getType();
    }

    public boolean isServerMessage() {
        return message != null && Type.JOIN == getType() || Type.PART == getType() || Type.QUIT == getType();
    }

    @PrePersist
    public void upperNick() {
        upperNick = nick == null ? null : nick.toUpperCase();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Logs{");
        sb.append("id=").append(id);
        sb.append('}');
        return sb.toString();
    }
}