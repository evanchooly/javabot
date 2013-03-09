package javabot.model;

import java.io.Serializable;
import java.util.Date;

import com.antwerkz.maven.SPI;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Indexes;
import org.bson.types.ObjectId;

@Entity("shun")
/*
@NamedQueries({
    @NamedQuery(name = ShunDao.BY_NAME, query = "select s from Shun s where s.nick = :nick"),
    @NamedQuery(name = ShunDao.CLEANUP, query = "delete from Shun s where s.expiry <= :now")
})
*/
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

  private Date expiry;

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

  public Date getExpiry() {
    return expiry;
  }

  public void setExpiry(Date updated) {
    this.expiry = updated;
  }

  public String getUpperNick() {
    return upperNick;
  }

  public void setUpperNick(final String upperNick) {
    this.upperNick = upperNick;
  }
}
