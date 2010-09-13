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

import com.antwerkz.maven.SPI;
import javabot.dao.KarmaDao;

@Entity
@Table(name = "karma")
@NamedQueries({
    @NamedQuery(name= KarmaDao.ALL, query= "from Karma k order by k.name"),
    @NamedQuery(name= KarmaDao.COUNT, query= "select count(*) from Karma"),
    @NamedQuery(name= KarmaDao.BY_NAME, query="from Karma k where k.name = :name")
})
@SPI(Persistent.class)
public class Karma implements Serializable, Persistent {
    private Long id;
    private String name;
    private Integer value = 0;
    private String userName;
    private Date updated;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long karmId) {
        id = karmId;
    }

    public String getName() {
        return name;
    }

    public void setName(String karmaName) {
        name = karmaName;
    }

    @Column
    public Integer getValue() {
        return value;
    }

    public void setValue(Integer karmaValue) {
        value = karmaValue;
    }

    @Column(length = 100)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String usrName) {
        userName = usrName;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date date) {
        updated = date;
    }
}