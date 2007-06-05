package javabot.dao.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

//
// User: joed
// Date: Apr 11, 2007
// Time: 2:22:19 PM

//
@Entity
@Table(name = "logs")
public class Logs implements Serializable {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "`nick`")
    private String nick;
    @Column(name = "`channel`")
    private String channel;
    @Column(name = "`message`", length = 4000)
    private String message;
    @Column(name = "`updated`")
    private Date updated;

    public enum Type {
        JOIN, PART, QUIT, ACTION, KICK, BAN, MESSAGE
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "`type`")
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public boolean isAction() {
        return message != null && Type.ACTION.compareTo(getType()) == 0;
    }

    public Type getType() {
        return Type.valueOf(type);
    }

    public void setType(Type type) {
        this.type = type.toString();
    }

    public boolean isKick() {
        return message != null && Type.KICK.compareTo(getType()) == 0;
    }

    public boolean isServerMessage() {
        return message != null && Type.JOIN.compareTo(getType()) == 0 || Type.PART.compareTo(getType()) == 0
            || Type.QUIT.compareTo(getType()) == 0;
    }

}