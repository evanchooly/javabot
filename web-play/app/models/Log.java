package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
        NICK,
    }

    @Enumerated(EnumType.STRING)
    private Type type;

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
            System.out.println("start = " + start);
            System.out.println("end = " + end);
            logs= Log.find("channel = ? and updated >= ? and updated < ?", name, start, end).fetch();
        } else {
            logs = Collections.<Log>emptyList();
        }
        return logs;
    }
}
