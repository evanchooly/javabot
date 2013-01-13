package javabot.javadoc;

import com.antwerkz.maven.SPI;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import javabot.model.Persistent;

@Entity("fields")
/*
@NamedQueries({
    @NamedQuery(name = ClazzDao.GET_FIELD_WITH_CLS_AND_PKG, query = "select f from Field f join f.clazz c where "
        + " upper(c.packageName)=:packageName and upper(c.className)=:className "
        + " and upper(f.name)=:fieldName order by c.packageName, c.className, f.name"),
    @NamedQuery(name = ClazzDao.GET_FIELD_WITH_CLS, query = "select f from Field f join f.clazz c where "
        + " upper(c.className)=:className and upper(f.name)=:fieldName order by c.packageName, c.className, f.name"),

    @NamedQuery(name = ClazzDao.GET_FIELD_WITH_CLS_PKG_API, query = "select f from Field f join f.clazz c where "
        + " upper(c.packageName)=:packageName and upper(c.className)=:className and c.api.id=:api"
        + " and upper(f.name)=:fieldName order by c.packageName, c.className, f.name"),
    @NamedQuery(name = ClazzDao.GET_FIELD_WITH_CLS_API, query = "select f from Field f join f.clazz c join c.api a where a.id=:api "
        + " and upper(c.className)=:className and upper(f.name)=:fieldName order by c.packageName, c.className, f.name")
})
*/
@SPI(Persistent.class)
public class Field extends JavadocElement {
    @Id
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
    public String getApiName() {
        return getClazz().getApi().getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long methodId) {
        id = methodId;
    }

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