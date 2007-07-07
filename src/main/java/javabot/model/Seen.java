package javabot.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javabot.dao.SeenDao;

@Entity
@Table(name = "seen", uniqueConstraints = @UniqueConstraint(columnNames = {"nick", "channel"}))
@NamedQueries({
    @NamedQuery(name=SeenDao.BY_NAME_AND_CHANNEL, query="select s from Seen s where s.nick = :nick AND s.channel = :channel")
})
public class Seen implements Serializable, Persistent {

    private Long id;
    private String nick;
    private String channel;
    private String message;
    private Date updated;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long seenId) {
        id = seenId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String name) {
        nick = name;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channelName) {
        channel = channelName;
    }

    @Column(length = 2000)
    public String getMessage() {
        return message;
    }

    public void setMessage(String seenMessage) {
        message = seenMessage;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date date) {
        updated = date;
    }
}