package javabot.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.AlsoLoad;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(value = "admins", noClassnameStored = true)
@Indexes({
             @Index(value = "emailAddress", unique = true),
             @Index("ircName, hostName")
         })
public class Admin implements Serializable, Persistent {
    @Id
    private ObjectId id;

    private Boolean botOwner = false;

    private String hostName;

    private String ircName;

    @AlsoLoad("userName")
    private String emailAddress;

    private String addedBy;

    private LocalDateTime updated = LocalDateTime.now();

    @Override
    public ObjectId getId() {
        return id;
    }

    @Override
    public void setId(ObjectId adminId) {
        id = adminId;
    }

    public Boolean getBotOwner() {
        return botOwner;
    }

    public void setBotOwner(Boolean botOwner) {
        this.botOwner = botOwner;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String address) {
        emailAddress = address;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime date) {
        updated = date;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getIrcName() {
        return ircName;
    }

    public void setIrcName(String ircName) {
        this.ircName = ircName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Admin{");
        sb.append("id=").append(id);
        sb.append(", botOwner=").append(botOwner);
        sb.append(", hostName='").append(hostName).append('\'');
        sb.append(", emailAddress='").append(emailAddress).append('\'');
        sb.append(", ircName='").append(ircName).append('\'');
        sb.append(", addedBy='").append(addedBy).append('\'');
        sb.append(", updated=").append(updated);
        sb.append('}');
        return sb.toString();
    }
}