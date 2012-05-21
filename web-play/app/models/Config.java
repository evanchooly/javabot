package models;

import java.util.Set;
import java.util.TreeSet;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

import controllers.AdminController;
import org.hibernate.annotations.CollectionOfElements;
import play.db.jpa.Model;

@Entity
@Table(name = "configuration")
public class Config extends Model {
  public String server;
  public Integer port;
  public Integer historyLength;
  public String trigger;
  public String url;
  public String nick;
  public String password;
  public Integer schemaVersion;
  @CollectionOfElements(fetch = FetchType.EAGER)
  public Set<String> operations = new TreeSet<String>();

  public static Config createConfig() {
    Config config = new Config();
    config.port = 6667;
    config.server = "irc.freenode.org";
    config.historyLength = 6;
    config.trigger = "~";

    return config;
  }
  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("Config {");
    sb.append(" server='").append(server).append('\'');
    sb.append(", port=").append(port);
    sb.append(", url=").append(url);
    sb.append(", nick='").append(nick).append('\'');
    sb.append(", trigger='").append(trigger).append('\'');
    sb.append(", historyLength=").append(historyLength);
    sb.append(", password='*******'");
    sb.append(", operations=").append(operations);
    sb.append('}');
    return sb.toString();
  }

  public static void updateOperations(Set<String> old, Set<String> updated) {
    for (String operation : AdminController.OPERATIONS) {
      EventType type = null;
      if(old.contains(operation) && !updated.contains(operation)) {
        type = EventType.DELETE;
      } else if (!old.contains(operation) && updated.contains(operation)) {
        type = EventType.ADD;
      }
      if(type != null) {
        OperationEvent event = new OperationEvent(type, operation, AdminController.getTwitterContext().screenName);
        event.save();
      }
    }
  }
}
