package javabot.model;

import java.io.Serializable;
import java.util.Date;

import com.antwerkz.maven.SPI;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Indexes;
import com.google.code.morphia.annotations.PrePersist;
import org.bson.types.ObjectId;

@Entity("karma")
/*
@NamedQueries({
    @NamedQuery(name= KarmaDao.ALL, query= "from Karma k order by k.name"),
    @NamedQuery(name= KarmaDao.COUNT, query= "select count(*) from Karma"),
    @NamedQuery(name= KarmaDao.BY_NAME, query="from Karma k where k.name = :name")
})
*/
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

  private Date updated;

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

  public Date getUpdated() {
    return updated;
  }

  public void setUpdated(Date date) {
    updated = date;
  }

  @PrePersist
  public void uppers() {
    upperName = name.toUpperCase();
  }
}