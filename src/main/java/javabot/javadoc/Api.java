package javabot.javadoc;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javabot.dao.ApiDao;
import javabot.model.Persistent;

/**
 * Created Oct 29, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@Entity
@Table(name = "apis")
@NamedQueries({
    @NamedQuery(name = ApiDao.FIND_BY_NAME, query="select a from Api a where a.name=:name"),
    @NamedQuery(name = ApiDao.LIST_NAMES, query="select a.name from Api a order by a.name")
})
public class Api implements Persistent {
    private Long id;
    private String name;
    private String baseUrl;
    private List<Clazz> classes;

    public Api() {
    }

    public Api(String apiName, String url) {
        name = apiName;
        baseUrl = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length=1000)
    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "api")
    public List<Clazz> getClasses() {
        return classes;
    }

    public void setClasses(List<Clazz> classes) {
        this.classes = classes;
    }
}
