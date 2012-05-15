package models;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.jpa.Model;

@Entity
@Table(name = "logs")
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

  public static List<Log> findByChannel(String name, Date date, Boolean showAll) {
    Channel channel = Channel.find("byName", name).first();
    List<Log> logs;
    if (showAll || channel.logged) {
      Calendar day = Calendar.getInstance();
      day.setTime(date);
      day.clear(Calendar.HOUR);
      day.clear(Calendar.MINUTE);
      day.clear(Calendar.SECOND);
      day.clear(Calendar.MILLISECOND);
      Date start = day.getTime();
      day.add(Calendar.DATE, 1);
      day.add(Calendar.MILLISECOND, -1);
      Date end = day.getTime();
      logs = Log.find("channel = ? and updated >= ? and updated < ?", name, start, end).fetch();
    } else {
      logs = Collections.<Log>emptyList();
    }
    return logs;
  }
}
