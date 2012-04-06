package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "channel")
public class Channel extends Model {
    String name;
    String key;
    Date updated;
    Boolean logged = true;

    public static List<Channel> findLogged() {
        return Channel.find("logged = true order by name").fetch();
    }
}
