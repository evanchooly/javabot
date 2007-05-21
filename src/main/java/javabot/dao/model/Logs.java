package javabot.dao.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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
    // For MySQL uncomment the AUTO strategy
    // Postgresql had an existing sequence
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(generator = "seen_sequence")
    @SequenceGenerator(name = "seen_sequence", sequenceName = "seen_sequence", allocationSize = 1)
    private Long id;


    @Column(name = "`nick`")
    private String nick;

    @Column(name = "`channel`")
    private String channel;

    @Column(name = "`message`", length = 4000)
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