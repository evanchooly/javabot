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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javabot.dao.FactoidDao;

@Entity
@Table(name = "factoids")
@NamedQueries({
    @NamedQuery(name= FactoidDao.ALL, query="select f from Factoid f"),
    @NamedQuery(name= FactoidDao.COUNT, query= "select count(f) from Factoid f"),
    @NamedQuery(name= FactoidDao.BY_NAME, query= "select m from Factoid m where lower(m.name) = :name")

})
public class Factoid implements Serializable, Persistent {
    private Long id;
    private String name;
    private String value;
    private String userName;
    private Date updated;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long factoidId) {
        id = factoidId;
    }

    public String getName() {
        return name;
    }

    public void setName(String factoidName) {
        name = factoidName;
    }

    @Column(length = 2000)
    public String getValue() {
        return value;
    }

    public void setValue(String factoidValue) {
        value = factoidValue;
    }

    @Column(length = 100)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String user) {
        userName = user;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date date) {
        updated = date;
    }
}
