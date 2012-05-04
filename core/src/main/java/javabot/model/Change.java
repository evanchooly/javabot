package javabot.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.antwerkz.maven.SPI;
import org.hibernate.annotations.Index;

@Entity
@Table(name = "changes")
@SPI(Persistent.class)
public class Change implements Serializable, Persistent {

    private Long id;
    private String message;
    private Date changeDate;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "`message`", length = 2000)
    public String getMessage() {
        return message;
    }

    public void setMessage(String value) {
        message = value;
    }

    @Index(name = "changed")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date date) {
        changeDate = date;
    }
}
