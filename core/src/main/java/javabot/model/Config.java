package javabot.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.antwerkz.maven.SPI;
import javabot.dao.ConfigDao;
import org.hibernate.annotations.CollectionOfElements;

/**
 * Created Jun 17, 2007
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@Entity
@Table(name = "configuration")
@NamedQueries({
    @NamedQuery(name = ConfigDao.GET_CONFIG, query = "select c from Config c")
})
@SPI(Persistent.class)
public class Config implements Serializable, Persistent {
    private Long id;
    private String server = "irc.freenode.org";
    private Integer port = 6667;
    private Integer historyLength = 6;
    private String prefixes = "~";
    private String nick;
    private String password;
    private Set<String> operations = new TreeSet<String>();

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(final Long configId) {
        id = configId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(final String botName) {
        nick = botName;
    }

    @SuppressWarnings({"JpaModelErrorInspection"})
    @CollectionOfElements(fetch = FetchType.EAGER)
    public Set<String> getOperations() {
        return operations;
    }

    public void setOperations(final Set<String> list) {
        operations = list;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String value) {
        password = value;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(final Integer portNum) {
        port = portNum;
    }

    public String getPrefixes() {
        return prefixes;
    }

    public void setPrefixes(final String nicks) {
        prefixes = nicks;
    }

    public String getServer() {
        return server;
    }

    public void setServer(final String ircServer) {
        server = ircServer;
    }

    public Integer getHistoryLength() {
        return historyLength;
    }

    public void setHistoryLength(final Integer historyLength) {
        this.historyLength = historyLength;
    }

    @Override
    public String toString() {
        return "Config{" +
            "server='" + server + '\'' +
            ", port=" + port +
            ", prefixes='" + prefixes + '\'' +
            ", nick='" + nick + '\'' +
            ", password='#######'" +
            ", historyLength=" + historyLength +
            '}';
    }
}
