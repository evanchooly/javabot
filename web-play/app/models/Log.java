package models;

import play.db.jpa.Model;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        NICK,;
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

    public static List<Log> findByChannel(String name, String date) {
        Channel channel = Channel.find("byName", name).first();
        List<Log> logs;
        if (channel.logged) {
            Calendar day = Calendar.getInstance();
            try {
                day.setTime(new SimpleDateFormat("yyyy.MM.dd").parse(date));
            } catch (ParseException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
            day.clear(Calendar.HOUR);
            day.clear(Calendar.MINUTE);
            day.clear(Calendar.SECOND);
            day.clear(Calendar.MILLISECOND);

            Date start = day.getTime();
            day.add(Calendar.DATE, 1);
            day.add(Calendar.MILLISECOND, -1);
            Date end = day.getTime();
            logs= Log.find("channel = ? and updated >= ? and updated < ?", name, start, end).fetch();
        } else {
            logs = Collections.<Log>emptyList();
        }
        return logs;
    }
}
