package javabot.model;

import com.fasterxml.jackson.annotation.JsonView;
import javabot.json.Views.PUBLIC;
import javabot.operations.TellSubject;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.PrePersist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.LocalDateTime;

import static java.lang.String.format;

@Entity(value = "factoids", noClassnameStored = true)
@Indexes({
             @Index(value = "upperName", unique = true),
             @Index("upperName, upperUserName")
})
public class Factoid implements Serializable, Persistent {
    private static final Logger log = LoggerFactory.getLogger(Factoid.class);
    @Id
    private ObjectId id;
    private String name;
    private String value;
    private String userName;
    @JsonView(PUBLIC.class)
    private LocalDateTime updated;
    private LocalDateTime lastUsed;
    private Boolean locked;
    private String upperName;
    private String upperUserName;
    private String upperValue;

    public Factoid() {
    }

    public Factoid(final String name, final String value, final String userName) {
        this.name = name;
        this.value = value;
        this.userName = userName;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(final ObjectId factoidId) {
        id = factoidId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String creator) {
        userName = creator;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(final LocalDateTime date) {
        updated = date;
    }

    public LocalDateTime getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(final LocalDateTime used) {
        lastUsed = used;
    }

    public Boolean getLocked() {
        return locked == null ? Boolean.FALSE : locked;
    }

    public void setLocked(final Boolean locked) {
        this.locked = locked;
    }

    public String evaluate(final TellSubject subject, final String sender, final String replacedValue) {
        String message = getValue();
        final String target = subject == null ? sender : subject.getTarget().getNick();
        if (subject != null && !message.contains("$who") && message.startsWith("<reply>")) {
            message = new StringBuilder(message).insert(message.indexOf(">") + 1, "$who, ").toString();
        }
        message = message.replaceAll("\\$who", target);
        String replaced = replacedValue;
        if (getName().endsWith("$1")) {
            replaced = replacedValue;
        }
        if (getName().endsWith(" $+")) {
            replaced = urlencode(replacedValue);
        }
        if (getName().endsWith(" $^")) {
            replaced = urlencode(camelcase(replacedValue));
        }
        if (replacedValue != null) {
            message = message.replaceAll("\\$1", replaced);
            message = message.replaceAll("\\$\\+", replaced);
            message = message.replaceAll("\\$\\^", replaced);
        }
        message = processRandomList(message);
        if (!message.startsWith("<")) {
            message = (subject == null ? sender : subject.getTarget()) + ", " + getName() + " is " + message;
        }
        return message;
    }

    private String urlencode(final String in) {
        try {
            return URLEncoder.encode(in, Charset.defaultCharset().displayName());
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
            return in;
        }
    }

    private String camelcase(final String in) {
        final StringBuilder sb = new StringBuilder(in.replaceAll("\\s", " "));
        if (!in.isEmpty()) {
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
                result = format("%s%s%s", result.substring(0, index), choices[chosen],
                                result.substring(index2 + 1));
            }
            index = result.indexOf("(", index + 1);
            index2 = result.indexOf(")", index + 1);
        }
        return result;
    }

    @PrePersist
    public void uppers() {
        upperName = name.toUpperCase();
        upperUserName = userName.toUpperCase();
        upperValue = value.toUpperCase();
    }

    @Override
    public String toString() {
        return format("Factoid{id=%s, name='%s', value='%s', userName='%s', updated=%s, lastUsed=%s, locked=%s}",
                      id, name, value, userName, updated, lastUsed, locked);
    }
}