package javabot.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.CascadeType;

@Entity
@Table(name = "channel")
@NamedQueries({
    @NamedQuery(name="Channel.all", query= "from Channel c order by c.name"),
    @NamedQuery(name="Channel.byName", query= "from Channel c where c.name = :channel")
})
public class Channel implements Serializable {
    private Long id;
    private String name;
    private Date updated;
    private Boolean logged;
    private Config config;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long channelId) {
        this.id = channelId;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date date) {
        this.updated = date;
    }

    public Boolean getLogged() {
        return logged;
    }

    public void setLogged(Boolean isLogged) {
        this.logged = isLogged;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Channel{" +
            "id=" + id +
            ", logged=" + logged +
            ", name='" + name + '\'' +
            ", updated=" + updated +
            '}';
    }
}