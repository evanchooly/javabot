package models;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name = "factoids")
public class Factoid extends Model {
    public String name;
    public String value;
    public String userName;
    public Date updated;
    public Date lastUsed;
    public Boolean locked;

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("Factoid {");
    sb.append(" lastUsed=").append(lastUsed);
    sb.append(", name='").append(name).append('\'');
    sb.append(", value='").append(value).append('\'');
    sb.append(", locked=").append(locked);
    sb.append(", userName='").append(userName).append('\'');
    sb.append(", updated=").append(updated);
    sb.append('}');
    return sb.toString();
  }
}
