package javabot.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javabot.dao.FactoidDao;
import javabot.operations.TellSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "factoids")
@NamedQueries({
    @NamedQuery(name= FactoidDao.ALL, query="select f from Factoid f"),
    @NamedQuery(name= FactoidDao.COUNT, query= "select count(f) from Factoid f"),
    @NamedQuery(name= FactoidDao.BY_NAME, query= "select m from Factoid m where lower(m.name) = :name")

})
public class Factoid implements Serializable, Persistent {
    private static final Logger log = LoggerFactory.getLogger(Factoid.class);

    private Long id;
    private String name;
    private String value;
    private String userName;
    private Date updated;
    private Date lastUsed;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(final Long factoidId) {
        id = factoidId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Column(length = 2000)
    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    @Column(length = 100)
    public String getUserName() {
        return userName;
    }

    public void setUserName(final String creator) {
        userName = creator;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(final Date date) {
        updated = date;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(final Date used) {
        lastUsed = used;
    }

    public String evaluate(final TellSubject subject, final String sender, final String replacedValue) {
        String message = getValue();
        String target = subject == null ? sender : subject.getTarget();
        if(subject != null && !message.contains("$who") && message.startsWith("<reply>")) {
            message = new StringBuilder(message).insert(message.indexOf(">") + 1, "$who, ").toString();
        }
        message = message.replaceAll("\\$who", target);
        String replaced = replacedValue;
        if (getName().endsWith( "$1")) {
            replaced = replacedValue;
        }
        if (getName().endsWith(" $+")) {
            replaced = urlencode(replacedValue);
        }
        if ( getName().endsWith(" $^")) {
            replaced = urlencode(camelcase(replacedValue));
        }
        if (replacedValue != null) {
            message = message.replaceAll("\\$1", replaced);
            message = message.replaceAll("\\$\\+", replaced);
            message = message.replaceAll("\\$\\^", replaced);
        }
        message = processRandomList(message);
        if(!message.startsWith("<")) {
            message = (subject == null ? sender : subject.getTarget()) + ", " + getName() + " is " + message;
        }
        return message;
    }

    private String urlencode(String in) {
        try {
            return URLEncoder.encode(in, Charset.defaultCharset().displayName());
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
            return in;
        }
    }

    private String camelcase(String in) {
        StringBuilder sb = new StringBuilder(in.replaceAll("\\s", " "));
        if (in.length() != 0) {
            int idx = sb.indexOf(" ");
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            while (idx > -1) {
                sb.deleteCharAt(idx);
                if (idx < sb.length()) {
                    sb.setCharAt(idx, Character.toUpperCase(sb.charAt(idx)));
                }
                idx = sb.indexOf(" ");
            }
        }
        return sb.toString();
    }

    protected String processRandomList(final String message) {
        String result = message;
        int index = -1;
        index = result.indexOf("(", index + 1);
        int index2 = result.indexOf(")", index + 1);
        while (index < result.length() && index != -1 && index2 != -1) {
            final String choice = result.substring(index + 1, index2);
            final String[] choices = choice.split("\\|");
            if (choices.length > 1) {
                final int chosen = (int) (Math.random() * choices.length);
                result = String.format("%s%s%s", result.substring(0, index), choices[chosen],
                    result.substring(index2 + 1));
            }
            index = result.indexOf("(", index + 1);
            index2 = result.indexOf(")", index + 1);
        }
        return result;
    }

}