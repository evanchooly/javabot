package javabot.model;

import java.io.Serializable;
import java.util.Date;

import com.antwerkz.maven.SPI;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import javabot.Javabot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity( "channel")
@SPI(Persistent.class)
public class Channel implements Serializable, Persistent {
    private static final Logger log = LoggerFactory.getLogger(Channel.class);
    @Id
    private Long id;
    private String name;
    private String key;
    private Date updated;
    private Boolean logged = true;

    public Long getId() {
        return id;
    }

    public void setId(final Long channelId) {
        id = channelId;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(final Date date) {
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

    public void join(final Javabot bot) {
        bot.join(name, key);
    }
    public void leave(final Javabot bot, final String reason) {
        bot.leave(name, reason);
    }
}