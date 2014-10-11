package javabot.model;

import com.antwerkz.maven.SPI;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.PrePersist;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(value = "karma", noClassnameStored = true)
@SPI(Persistent.class)
@Indexes({
             @Index("upperName")
         })
public class Karma implements Serializable, Persistent {
    @Id
    private ObjectId id;

    private String name;

    private String upperName;

    private Integer value = 0;

    private String userName;

    private LocalDateTime updated;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId karmaId) {
        id = karmaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String karmaName) {
        name = karmaName;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer karmaValue) {
        value = karmaValue;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String usrName) {
        userName = usrName;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime date) {
        updated = date;
    }

    @PrePersist
    public void uppers() {
        upperName = name.toUpperCase();
    }
}