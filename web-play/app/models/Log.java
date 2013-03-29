package models;

import java.util.Date;
import java.util.EnumSet;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import com.google.code.morphia.annotations.Entity;
import play.db.jpa.Model;

@Entity
("logs")
public class Log extends Model {
  private String nick;
  private String channel;
  private String message;
  private Date updated;

  public enum Type {
    ACTION,
    BAN,
    DISCONNECTED,
    ERROR,
    INVITE,
    JOIN,
    PART,
    KICK,
    MESSAGE,
    QUIT,
    REGISTERED,
    TOPIC,
    NICK;
    private static final EnumSet<Type> SERVER_SET = EnumSet.of(JOIN, PART, QUIT);
  }

  @Enumerated(EnumType.STRING)
  private Type type;

  @Transient
  public boolean isAction() {
    return message != null && Type.ACTION == type;
  }

  @Transient
  public boolean isKick() {
    return message != null && Type.KICK == type;
  }

  @Transient
  public boolean isServerMessage() {
    return message != null && Type.SERVER_SET.contains(type);
  }


}
