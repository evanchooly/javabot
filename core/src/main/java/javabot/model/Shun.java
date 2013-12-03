package javabot.model;

import java.io.Serializable;

import com.antwerkz.maven.SPI;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Indexes;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;

@Entity("shuns")
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

  private DateTime expiry;

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

  public DateTime getExpiry() {
    return expiry;
  }

  public void setExpiry(DateTime updated) {
    this.expiry = updated;
  }

  public String getUpperNick() {
    return upperNick;
  }

  public void setUpperNick(final String upperNick) {
    this.upperNick = upperNick;
  }
}
