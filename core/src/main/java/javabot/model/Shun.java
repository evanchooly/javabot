package javabot.model;

import com.antwerkz.maven.SPI;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Indexes;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(value = "shuns", noClassnameStored = true)
@SPI(Persistent.class)
@Indexes({
             @Index("upperNick")
         })
public class Shun implements Serializable, Persistent {
    @Id
    private ObjectId id;

    @Indexed(unique = true)
    private String nick;

    private String upperNick;

    private LocalDateTime expiry;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public LocalDateTime getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDateTime updated) {
        this.expiry = updated;
    }

    public String getUpperNick() {
        return upperNick;
    }

    public void setUpperNick(final String upperNick) {
        this.upperNick = upperNick;
    }
}
