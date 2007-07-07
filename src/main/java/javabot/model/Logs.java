package javabot.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import javabot.dao.LogsDao;

@Entity
@Table(name = "logs")
@NamedQueries({
    @NamedQuery(name= LogsDao.TODAY, query = "from Logs s WHERE s.channel=:channel AND s.updated < :tomorrow AND"
        + " s.updated > :today order by updated"),
    @NamedQuery(name=LogsDao.LOGGED_CHANNELS, query="select distinct s.channel from Logs s where s.channel like '#%'")
})
public class Logs implements Serializable, Persistent {
    private Long id;
    private String nick;
    private String channel;
    private String message;
    private Date updated;

    public enum Type {
        JOIN, PART, QUIT, ACTION, KICK, BAN, MESSAGE
    }

    @Enumerated(EnumType.STRING)
    private Type type;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long logsId) {
        id = logsId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String user) {
        nick = user;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String chanName) {
        channel = chanName;
    }

    @Column(length = 4000)
    public String getMessage() {
        return message;
    }

    public void setMessage(String logMessage) {
        message = logMessage;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date date) {
        updated = date;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type value) {
        type = value;
    }

    @Transient
    public boolean isAction() {
        return message != null && Type.ACTION == getType();
    }

    @Transient
    public boolean isKick() {
        return message != null && Type.KICK==getType();
    }

    @Transient
    public boolean isServerMessage() {
        return message != null && Type.JOIN == getType() || Type.PART == getType() || Type.QUIT == getType();
    }
}