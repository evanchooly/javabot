package javabot.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import javabot.Javabot;
import javabot.dao.ChannelDao;

@Entity
@Table(name = "channel")
@NamedQueries({
    @NamedQuery(name=ChannelDao.ALL, query= "select c from Channel c order by c.name"),
    @NamedQuery(name=ChannelDao.BY_NAME, query= "select c from Channel c where c.name = :channel"),
    @NamedQuery(name= ChannelDao.CONFIGURED_CHANNELS, query= "select distinct s.name from Channel s")
})
public class Channel implements Serializable, Persistent {
    private Long id;
    private String name;
    private String key;
    private Date updated;
    private Boolean logged = true;
    private Config config;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long channelId) {
        id = channelId;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date date) {
        updated = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getLogged() {
        return logged;
    }

    public void setLogged(Boolean isLogged) {
        logged = isLogged;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public Config getConfig() {
        return config;
    }

    public void setConfig(Config chanConfig) {
        config = chanConfig;
    }

    public String getName() {
        return name;
    }

    public void setName(String chanName) {
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

    public void join(Javabot bot) {
        if (getKey() == null) {
            bot.joinChannel(getName());
        } else {
            bot.joinChannel(getName(), getKey());
        }
    }
}