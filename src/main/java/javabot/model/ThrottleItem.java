package javabot.model;

import java.util.Date;

import static java.lang.String.*;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.pircbotx.User;

@Entity(value = "throttled", noClassnameStored = true)
public class ThrottleItem implements Persistent {
  @Id
  private ObjectId id;
  private String user;
  @Indexed(expireAfterSeconds = 60)
  private Date when;

  public ThrottleItem(User user) {
      this.user = user.getNick();
      when = new Date();
  }

  @Override
  public ObjectId getId() {
    return id;
  }

  @Override
  public void setId(final ObjectId id) {
    this.id = id;
  }

  public String getUser() {
    return user;
  }

  public Date getWhen() {
    return when;
  }

  @Override
  public String toString() {
    return format("ThrottleItem{user='%s', when=%s}", user, when);
  }
}