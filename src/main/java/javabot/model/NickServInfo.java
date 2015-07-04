package javabot.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.pircbotx.User;

@Entity(value = "nickserv", noClassnameStored = true)
public class NickServInfo implements Persistent {
  public static final String NSERV_DATE_FORMAT = "MMM dd HH:mm:ss yyyy";

  @Id
  private ObjectId id;

  @Indexed
  private String nick;

  @Indexed
  private String account;

  private LocalDateTime registered;

  private LocalDateTime userRegistered;

  private LocalDateTime lastSeen;

  @Indexed(expireAfterSeconds = 60 * 60 * 24)
  private LocalDateTime created = now();

  private Map<String, String> extraneous = new TreeMap<>();

  private String lastAddress;

  public NickServInfo() {
  }

  public NickServInfo(final User user) {
    nick = user.getNick();
    account = null; //user.getUserName();
    registered = LocalDateTime.now();
    userRegistered = LocalDateTime.now();
  }

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public String getNick() {
    return nick;
  }

  public void setNick(final String nick) {
    this.nick = nick;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(final String account) {
    this.account = account;
  }

  public LocalDateTime getRegistered() {
    return registered;
  }

  public void setRegistered(final LocalDateTime registered) {
    this.registered = registered;
  }

  public LocalDateTime getUserRegistered() {
    return userRegistered;
  }

  public void setUserRegistered(final LocalDateTime userRegistered) {
    this.userRegistered = userRegistered;
  }

  public LocalDateTime getLastSeen() {
    return lastSeen;
  }

  public void setLastSeen(final LocalDateTime lastSeen) {
    this.lastSeen = lastSeen;
  }

  public String getLastAddress() {
    return lastAddress;
  }

  public void extra(String key, String value) {
    extraneous.put(key, value);
  }

  @Override
  public String toString() {
    return format("NickServInfo{id=%s, nick='%s', account='%s', registered=%s, userRegistered=%s, lastSeen=%s,"
        + " lastAddress='%s'}", id, nick, account, registered, userRegistered, lastSeen, lastAddress);
  }

  public List<String> toNickServFormat() {
//    "Information on cheeser (account cheeser):",
//    "Registered : Feb 20 21:31:56 2002 (12 years, 10 weeks, 2 days, 04:48:12 ago)",
//    "Last seen  : now",
//    "Flags      : HideMail, Private",
//    "cheeser has enabled nick protection",
//    "*** End of Info ***"
    List<String> list = new ArrayList<>();
    list.add(format("Information on %s (account %s):", getNick(), getAccount()));
    append(list, "Registered", toString(getRegistered()));
    append(list, "User Reg.", toString(getUserRegistered()));
    append(list, "Last seen", toString(getLastSeen()));
    list.add("*** End of Info ***");
    return list;
  }

  private void append(final List<String> list, final String label, final String value) {
    if (value != null) {
      list.add(format("%-12s: %s", label, value));
    }
  }

  private String toString(LocalDateTime date) {
    return date != null ? date.format(DateTimeFormatter.ofPattern(NSERV_DATE_FORMAT)) : null;
  }

  public void setLastAddress(final String lastAddress) {
    this.lastAddress = lastAddress;
  }
}
