package javabot.model;

import javabot.dao.ConfigDao;
import org.hibernate.annotations.CollectionOfElements;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
public class Config implements Serializable {
    private Long id;
    private String server;
    private Integer port;
    private String prefixes;
    private String nick;
    private String password;
    private Long version;

    private List<String> operations = new ArrayList<String>();
    private List<Channel> channels = new ArrayList<Channel>();

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long configId) {
        id = configId;
    }

    @OneToMany
    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> list) {
        channels = list;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String botName) {
        nick = botName;
    }

    @CollectionOfElements
    public List<String> getOperations() {
        return operations;
    }

    public void setOperations(List<String> list) {
        operations = list;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String value) {
        password = value;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer portNum) {
        port = portNum;
    }

    public String getPrefixes() {
        return prefixes;
    }

    public void setPrefixes(String nicks) {
        prefixes = nicks;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String ircServer) {
        server = ircServer;
    }

    @Version
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long value) {
        version = value;
    }
}
