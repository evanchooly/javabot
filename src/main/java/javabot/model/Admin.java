package javabot.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import javabot.dao.AdminDao;

@Entity
@Table(name = "admin")
@NamedQueries({
    @NamedQuery(name = AdminDao.AUTHENTICATE, query = "from Admin m where m.userName = :username"),
    @NamedQuery(name = AdminDao.ALL, query = "from Admin m order by userName")
})
public class Admin implements Serializable, Persistent {
    private Long id;
    private String userName;
    private String password;
    private String name;
    private Date updated;

    @Id
    @Column(name = "id")
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long adminId) {
        this.id = adminId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String adminName) {
        this.userName = adminName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pwd) {
        this.password = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date date) {
        this.updated = date;
    }
}