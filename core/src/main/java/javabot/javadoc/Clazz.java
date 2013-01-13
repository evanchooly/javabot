package javabot.javadoc;

import java.util.ArrayList;
import java.util.List;

import com.antwerkz.maven.SPI;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import javabot.model.Persistent;

@Entity ("classes")
/*
@NamedQueries({
    @NamedQuery(name = ClazzDao.DELETE_ALL, query = "delete from Clazz c where c.api=:api"),
    @NamedQuery(name = ClazzDao.DELETE_ALL_METHODS, query = "delete from Method m where m.clazz.api=:api"),
    @NamedQuery(name = ClazzDao.GET_BY_NAME, query = "select c from Clazz c where "
        + " upper(c.className)=:name"),
    @NamedQuery(name = ClazzDao.GET_BY_NAME_API, query = "select c from Clazz c where "
        + " upper(c.className)=:name and c.api.id=:api"),

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
*/
@SPI(Persistent.class)
public class Clazz extends JavadocElement {
    @Id
    private Long id;
    private Api api;
    private String packageName;
    @Indexed(name = "classNames")
    private String className;
    private Clazz superClass;
    private List<Method> methods = new ArrayList<>();
    private List<Field> fields = new ArrayList<>();

    public Clazz() {
    }

    public Clazz(final Api apiName, final String pkg, final String name) {
        api = apiName;
        packageName = pkg;
        className = name;
        setDirectUrl(apiName.getBaseUrl() + pkg.replace('.', '/') + "/" + name + ".html");
        setLongUrl(apiName.getBaseUrl() + "index.html?" + pkg.replace('.', '/') + "/" + name + ".html");
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long classId) {
        id = classId;
    }

    public Api getApi() {
        return api;
    }

    public void setApi(final Api api) {
        this.api = api;
    }

    @Override
    public String getApiName() {
        return getApi().getName();
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(final String name) {
        packageName = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(final String name) {
        className = name;
    }

    public Clazz getSuperClass() {
        return superClass;
    }

    public void setSuperClass(final Clazz aClass) {
        superClass = aClass;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(final List<Method> list) {
        methods = list;
    }

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