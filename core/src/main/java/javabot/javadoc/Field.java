package javabot.javadoc;

import javax.persistence.Transient;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import javabot.dao.ClazzDao;

@Entity
@Table(name = "fields")
@NamedQueries({
    @NamedQuery(name = ClazzDao.GET_FIELD_WITH_CLS_AND_PKG, query = "select f from Field f join f.clazz c where "
        + " upper(c.packageName)=:packageName and upper(c.className)=:className "
        + " and upper(f.name)=:fieldName order by c.packageName, c.className, f.name"),
    @NamedQuery(name = ClazzDao.GET_FIELD_WITH_CLS, query = "select f from Field f join f.clazz c where "
        + " upper(c.className)=:className and upper(f.name)=:fieldName order by c.packageName, c.className, f.name")
})
public class Field extends JavadocElement {
    private Long id;
    private Clazz clazz;
    private String name;
    private String type;

    public Field(final Clazz parent, final String fieldName, final String fieldType) {
        clazz = parent;
        name = fieldName;
        type = fieldType;
        final String url = clazz.getDirectUrl() + "#" + name;
        setLongUrl(url);
        setDirectUrl(url);
    }

    public Field() {
    }

    @Override
    @Transient
    public String getApiName() {
        return getClazz().getApi().getName();
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(final Long methodId) {
        id = methodId;
    }

    @ManyToOne
    public Clazz getClazz() {
        return clazz;
    }

    public void setClazz(final Clazz clazz) {
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return clazz + "." + name + ":" + type;
    }

}