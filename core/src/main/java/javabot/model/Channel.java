package javabot.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.antwerkz.maven.SPI;
import javabot.Javabot;
import javabot.dao.ChannelDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "channel")
@NamedQueries({
    @NamedQuery(name = ChannelDao.ALL, query = "select c from Channel c order by c.name"),
    @NamedQuery(name = ChannelDao.LOGGED_CHANNELS, query = "select c.name from Channel c where c.logged is true order by c.name"),
    @NamedQuery(name = ChannelDao.BY_NAME, query = "select c from Channel c where lower(c.name) = :channel"),
    @NamedQuery(name = ChannelDao.CONFIGURED_CHANNELS, query = "select distinct s.name from Channel s"),
    @NamedQuery(name = ChannelDao.STATISTICS, query = "select new javabot.Activity(l.channel, count(l), max(l.updated),"
        + " min(l.updated), (select count(e) from Logs e)) from Logs l "
        + "where l.channel like '#%' group by l.channel order by count(l) desc")
})
@SPI(Persistent.class)
public class Channel implements Serializable, Persistent {
    private static final Logger log = LoggerFactory.getLogger(Channel.class);
    private Long id;
    private String name;
    private String key;
    private Date updated;
    private Boolean logged = true;

    @Id
    @GeneratedValue
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