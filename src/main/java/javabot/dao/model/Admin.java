package javabot.dao.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

//
// User: joed
// Date: Apr 11, 2007

// Time: 2:22:19 PM
@Entity
@Table(name = "admin")
public class Admin implements Serializable {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "`username`")
    private String username;
    @Column(name = "`password`")
    private String password;
    @Column(name = "`fullname`")
    private String channel;
    @Column(name = "`updated`")
    private Date updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}