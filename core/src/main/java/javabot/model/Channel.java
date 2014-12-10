package javabot.model;

import com.fasterxml.jackson.annotation.JsonView;
import javabot.json.Views.PUBLIC;
import javabot.json.Views.SYSTEM;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.PrePersist;
import org.pircbotx.PircBotX;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(value = "channels", noClassnameStored = true)
@Indexes({
             @Index(value = "upperName", unique = true, dropDups = true)
         })
public class Channel implements Serializable, Persistent {
    @Id
    private ObjectId id;

    @JsonView(PUBLIC.class)
    private String name;

    @JsonView(SYSTEM.class)
    private String upperName;

    @JsonView(PUBLIC.class)
    private String key;

    @JsonView(PUBLIC.class)
    private LocalDateTime updated;

    @JsonView(PUBLIC.class)
    private Boolean logged = true;

    public Channel() {
    }

    public Channel(final String name, final String key, final boolean logged) {
        this.name = name;
        this.key = key;
        this.logged = logged;
    }

    public Channel(final ObjectId id, final String name, final String key, final boolean logged) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.logged = logged;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(final ObjectId channelId) {
        id = channelId;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(final LocalDateTime date) {
        updated = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public Boolean getLogged() {
        return logged == null ? Boolean.FALSE : logged;
    }

    public void setLogged(final Boolean isLogged) {
        logged = isLogged;
    }

    public String getName() {
        return name;
    }

    public void setName(final String chanName) {
        name = chanName;
    }

    @Override
    public String toString() {
        return "Channel{" +
               "id=" + id +
               ", logged=" + logged +
               ", name='" + name + '\'' +
               ", updated=" + updated +
               '}';
    }

    public String getUpperName() {
        return upperName;
    }

    public void setUpperName(final String upperName) {
        this.upperName = upperName;
    }

    @PrePersist
    public void uppers() {
        upperName = name.toUpperCase();
    }

    public void join(final PircBotX ircBot) {
        if(key == null) {
            ircBot.sendIRC().joinChannel(name);
        } else {
            ircBot.sendIRC().joinChannel(name, key);
        }
    }
}