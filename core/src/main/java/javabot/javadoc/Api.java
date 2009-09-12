package javabot.javadoc;

import java.util.ArrayList;
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
    @NamedQuery(name = ApiDao.FIND_BY_NAME, query="select a from Api a where upper(a.name)=upper(:name)"),
    @NamedQuery(name = ApiDao.FIND_ALL, query="select a from Api a order by a.name")
})
public class Api implements Persistent {
    private Long id;
    private String name;
    private String baseUrl;
    private String packages;
    private String zipLocations;
    private List<Clazz> classes = new ArrayList<Clazz>();

    public Api() {
    }

    public Api(final String apiName, final String url, String pkgs, String zip) {
        name = apiName;
        baseUrl = url.endsWith("/") ? url : url + "/";
        packages = pkgs;
        zipLocations = zip;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Column(length=1000)
    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "api")
    public List<Clazz> getClasses() {
        return classes;
    }

    public void setClasses(final List<Clazz> classes) {
        this.classes = classes;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(final String packages) {
        this.packages = packages;
    }

    public String getZipLocations() {
        return zipLocations;
    }

    public void setZipLocations(final String zipLocations) {
        this.zipLocations = zipLocations;
    }
}
