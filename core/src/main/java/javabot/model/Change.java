package javabot.model;

import java.io.Serializable;

import com.antwerkz.maven.SPI;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;

@Entity("changes")
@SPI(Persistent.class)
public class Change implements Serializable, Persistent {

    @Id
    private ObjectId id;
    private String message;
    @Indexed(name = "changed")
    private DateTime changeDate;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String value) {
        message = value;
    }

    public DateTime getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(DateTime DateTime) {
        changeDate = DateTime;
    }
}
