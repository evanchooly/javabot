package javabot.model;

import java.io.Serializable;
import java.util.Date;

import com.antwerkz.maven.SPI;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

@Entity("changes")
@SPI(Persistent.class)
public class Change implements Serializable, Persistent {

    @Id
    private Long id;
    private String message;
    @Indexed(name = "changed")
    private Date changeDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String value) {
        message = value;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date date) {
        changeDate = date;
    }
}
