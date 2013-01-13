package javabot.model;

import java.io.Serializable;
import java.util.Date;

import com.antwerkz.maven.SPI;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Indexes;

@Entity("logs")
@Indexes({
    @Index(value = "channel, updated, nick", name = "Logs")
})
/*
@NamedQueries({
    @NamedQuery(name = LogsDao.TODAY,
//        query = "select s from Logs s join s.channel c WHERE s.channel=:channel AND c.logged AND (s.updated between :today and"
        query = "select s from Logs s WHERE s.channel=:channel AND (s.updated between :today and"
            + " :tomorrow) and s.channel in (select c.name from Channel c where c.name=:channel and c.logged is true ) order by s.updated"),
    @NamedQuery(name = LogsDao.COUNT_LOGGED, query = "select count(s) from Logs s where s.channel like '#%'"),
    @NamedQuery(name = LogsDao.SEEN,
        query = "select new javabot.Seen(l.nick, l.message, l.channel, l.updated) from Logs l where"
            + " lower(l.nick) = :nick AND l.channel = :channel order by l.updated desc")
})
*/
@SPI(Persistent.class)
public class Logs implements Serializable, Persistent {
    @Id
    private Long id;
    private String nick;
    private String channel;
    private String message;
    private Date updated;

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
        REGISTERED, TOPIC, NICK,
    }

    private Type type;

    public Long getId() {
        return id;
    }

    public void setId(final Long logsId) {
        id = logsId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(final String user) {
        nick = user;
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

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(final Date date) {
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
}