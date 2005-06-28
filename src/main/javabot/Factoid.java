package javabot;

import java.util.Date;
import java.sql.ResultSet;

/**
 * Created Jun 28, 2005
 *
 * @author <a href="mailto:javabot@cheeseronline.org">Justin Lee</a>
 */
public class Factoid {
    private long _id;
    private String _name;
    private String _value;
    private String _user;
    private Date _updated;

    public Factoid(String name, String value, String user) {
        _name = name;
        _value = value;
        _user = user;
    }

    public Factoid(long id, String name, String value, String user, Date updated) {
        _name = name;
        _id = id;
        _value = value;
        _user = user;
        _updated = updated;
    }

    public long getID() {
        return _id;
    }

    public Date getUpdated() {
        return _updated;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getValue() {
        return _value;
    }

    public void setValue(String value) {
        _value = value;
    }

    public String getUser() {
        return _user;
    }

    public void setUser(String user) {
        _user = user;
    }
}