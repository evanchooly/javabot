package javabot.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "channel")
public class Channel implements Serializable {
    private Long id;
    private String name;
    private Date updated;
    private Boolean logged;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long channelId) {
        this.id = channelId;
    }

    public String getChannel() {
        return name;
    }

    public void setChannel(String channel) {
        name = channel;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date date) {
        this.updated = date;
    }

    public Boolean getLogged() {
        return logged;
    }

    public void setLogged(Boolean isLogged) {
        this.logged = isLogged;
    }
}