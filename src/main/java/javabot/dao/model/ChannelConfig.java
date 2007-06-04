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
@Table(name = "channelconfig",uniqueConstraints = @UniqueConstraint(columnNames = { "`channel`"}))
public class ChannelConfig implements Serializable {


    @Id
    @Column(name = "`id`")
    // For MySQL uncomment the AUTO strategy
    // Postgresql had an existing sequence
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(generator = "channels_sequence")
    @SequenceGenerator(name = "channels_sequence", sequenceName = "channels_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "`channel`")
    private String channel;

    @Column(name = "`updated`")
    private Date updated;

    @Column(name = "`logged`")
    private Boolean logged;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Boolean getLogged() {
        return logged;
    }

    public void setLogged(Boolean logged) {
        this.logged = logged;
    }
}