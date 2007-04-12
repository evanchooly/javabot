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
public class factoids implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;


    @Column(name = "`value`")
    private String value;

    @Column(name = "username")
    private String userName;

    @Column(name = "updated")
    private Date updated;


    public Long getSeqno() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }


}
