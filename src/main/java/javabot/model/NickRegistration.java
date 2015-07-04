package javabot.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.pircbotx.User;

import java.io.Serializable;
import java.util.UUID;

@Entity(value = "registrations", noClassnameStored = true)
public class NickRegistration implements Serializable, Persistent {
    @Id
    private ObjectId id;
    private String url;
    private String nick;
    private String host;
    private String twitterName;

    public NickRegistration() {
    }

    public NickRegistration(User sender, String twitterName) {
        this.twitterName = twitterName;
        url = UUID.randomUUID().toString();
        nick = sender.getNick();
        host = sender.getHostmask();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getTwitterName() {
        return twitterName;
    }

    public void setTwitterName(String twitterName) {
        this.twitterName = twitterName;
    }
}
