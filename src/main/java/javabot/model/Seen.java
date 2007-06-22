package javabot.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

//
// User: joed
// Date: Apr 11, 2007
// Time: 2:22:19 PM
@Entity
@Table(name = "seen", uniqueConstraints = @UniqueConstraint(columnNames = {"`nick`", "`channel`"}))
public class Seen implements Serializable {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "`nick`")
    private String nick;
    @Column(name = "`channel`")
    private String channel;
    @Column(name = "`message`", length = 2000)
    private String message;
    @Column(name = "`updated`")
    private Date updated;

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
}