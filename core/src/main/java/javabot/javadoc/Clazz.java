package javabot.javadoc;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.antwerkz.maven.SPI;
import javabot.dao.ClazzDao;
import javabot.model.Persistent;
import org.hibernate.annotations.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "classes")
@NamedQueries({
    @NamedQuery(name = ClazzDao.DELETE_ALL, query = "delete from Clazz c where c.api=:api"),
    @NamedQuery(name = ClazzDao.DELETE_ALL_METHODS, query = "delete from Method m where m.clazz.api=:api"),
    @NamedQuery(name = ClazzDao.GET_BY_NAME, query = "select c from Clazz c where "
        + " upper(c.className)=:name"),
    @NamedQuery(name = ClazzDao.GET_BY_API_PACKAGE_AND_NAME, query = "select c from Clazz c where "
        + " c.packageName=:package and c.api=:api and c.className=:name"),
    @NamedQuery(name = ClazzDao.GET_BY_PACKAGE_AND_NAME, query = "select c from Clazz c where "
        + " upper(c.packageName)=upper(:package) and upper(c.className)=upper(:name)"),
    @NamedQuery(name = ClazzDao.GET_METHOD_NO_SIG, query = "select m from Method m where "
        + "m.clazz.id=:classId and upper(m.methodName)=:name order by m.shortSignatureStripped"),
    @NamedQuery(name = ClazzDao.GET_METHOD, query = "select m from Clazz c join c.methods m where "
        + "m.clazz.id=:classId and upper(m.methodName)=:name and (upper(m.shortSignatureTypes)=:params"
        + " or upper(m.shortSignatureStripped)=:params or upper(m.longSignatureTypes)=:params"
        + " or upper(m.longSignatureStripped)=:params) order by m.shortSignatureStripped")
})
@SPI(Persistent.class)
public class Clazz extends JavadocElement implements Persistent {
    private static final Logger log = LoggerFactory.getLogger(Clazz.class);
    private Long id;
    private Api api;
    private String packageName;
    private String className;
    private Clazz superClass;
    private List<Method> methods = new ArrayList<Method>();
    private List<Field> fields = new ArrayList<Field>();

    public Clazz() {
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(final Long classId) {
        id = classId;
    }

    public Clazz(final Api apiName, final String pkg, final String name) {
        api = apiName;
        packageName = pkg;
        className = name;
        setDirectUrl(apiName.getBaseUrl() + pkg.replace('.', '/') + "/" + name + ".html");
        setLongUrl(apiName.getBaseUrl() + "index.html?" + pkg.replace('.', '/') + "/" + name + ".html");
    }

    @ManyToOne
    public Api getApi() {
        return api;
    }

    public void setApi(final Api api) {
        this.api = api;
    }

    @Override
    @Transient
    public String getApiName() {
        return getApi().getName();
    }

    @Column(nullable = false)
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(final String name) {
        packageName = name;
    }

    @Column(nullable = false)
    @Index(name = "ClazzName")
    public String getClassName() {
        return className;
    }

    public void setClassName(final String name) {
        className = name;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Clazz getSuperClass() {
        return superClass;
    }

    public void setSuperClass(final Clazz aClass) {
        superClass = aClass;
    }

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "clazz")
    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(final List<Method> list) {
        methods = list;
    }

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "clazz")
    public List<Field> getFields() {
        return fields;
    }

    public void setFields(final List<Field> list) {
        fields = list;
    }

    @Override
    public String toString() {
        return packageName + "." + className;
    }
}