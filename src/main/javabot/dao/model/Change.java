package javabot.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

//
// User: joed
// Date: Apr 11, 2007
// Time: 2:22:19 PM

//
@Entity
@Table(name = "change")
public class Change implements Serializable {


    @Column(name = "`message`", length = 2000)
    private String message;

    //This is slightly fugly, but realistically
    //not so likely that the exact timestamp is going to hit....

    @Id
    @Column(name = "`changedate`")
    private Date changeDate;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }
}
