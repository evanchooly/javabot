package javabot.dao.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @SequenceGenerator(name = "channels_sequence", sequenceName = "channels_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "`channel`")
    private String channel;

    @Temporal(TemporalType.TIMESTAMP)
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