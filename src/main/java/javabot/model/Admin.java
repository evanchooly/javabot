package javabot.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import javabot.dao.AdminDao;

@Entity
@Table(name = "admin")
@NamedQueries({
    @NamedQuery(name = AdminDao.AUTHENTICATE, query = "select a from Admin a where a.userName = :username"),
    @NamedQuery(name = AdminDao.FIND_ALL, query = "select a from Admin a order by a.userName")
})
public class Admin implements Serializable, Persistent {
    private Long id;
    private String userName;
    private String password;
    private Date updated;
    private Config config;
                                    
    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long adminId) {
        id = adminId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String adminName) {
        userName = adminName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pwd) {
        password = pwd;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date date) {
        updated = date;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public Config getConfig() {
        return config;
    }

    public void setConfig(Config chanConfig) {
        config = chanConfig;
    }
}