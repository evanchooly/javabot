package javabot.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

//
// User: joed
// Date: Apr 11, 2007
// Time: 2:22:19 PM

//
@Entity
@Table(name = "logs" )
public class Logs implements Serializable {

    @Id
    @Column(name = "`nick`")
    private String nick;

    @Column(name = "`channel`")
    private String channel;

    @Column(name = "`message`", length = 4000)
    private String message;

    @Column(name = "`updated`")
    private Date date;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date updated) {
        this.date = updated;
    }
}