package javabot.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.persistence.FetchType;

import javabot.dao.ConfigDao;
import org.hibernate.annotations.CollectionOfElements;

/**
 * Created Jun 17, 2007
 *
 * @author <a href="mailto:javabot@cheeseronline.org">cheeser</a>
 */
@Entity
@Table(name = "configuration")
@NamedQueries({
@NamedQuery(name = ConfigDao.GET_CONFIG, query = "select c from Config c")
        })
public class Config implements Serializable, Persistent {
    private Long id;
    private String server = "irc.freenode.org";
    private Integer port = 6667;
    private String prefixes = "~";
    private String nick = "test-javabot-" + System.currentTimeMillis();
    private String password;
    private Long version;

    private List<String> operations = new ArrayList<String>();
//    private List<Channel> channels = new ArrayList<Channel>();
    private List<Admin> admins = new ArrayList<Admin>();

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(final Long configId) {
        id = configId;
    }

//    @OneToMany(mappedBy = "config")
//    public List<Channel> getChannels() {
//        return channels;
//    }
//
//    public void setChannels(List<Channel> list) {
//        channels = list;
//    }

    @OneToMany(mappedBy = "config")
    public List<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(final List<Admin> list) {
        admins = list;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(final String botName) {
        nick = botName;
    }

    @SuppressWarnings({"JpaModelErrorInspection"})
    @CollectionOfElements(fetch = FetchType.EAGER)
    public List<String> getOperations() {
        return operations;
    }

    public void setOperations(final List<String> list) {
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

    @Version
    public Long getVersion() {
        return version;
    }

    public void setVersion(final Long value) {
        version = value;
    }
}
