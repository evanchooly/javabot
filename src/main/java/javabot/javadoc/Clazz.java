package javabot.javadoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.MethodDoc;
import javabot.dao.ClazzDao;
import javabot.model.Persistent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "classes")
@NamedQueries({
    @NamedQuery(name = ClazzDao.DELETE_ALL, query = "delete from Clazz c where c.api=:api"),
    @NamedQuery(name = ClazzDao.DELETE_ALL_METHODS, query = "delete from Method m where m.clazz.api=:api"),
    @NamedQuery(name = ClazzDao.GET_BY_NAME, query = "select c from Clazz c where "
        + "upper(c.className)=:name"),
    @NamedQuery(name = ClazzDao.GET_BY_API_PACKAGE_AND_NAME, query = "select c from Clazz c where "
        + "c.packageName=:package and c.api=:api and c.className=:name"),
    @NamedQuery(name = ClazzDao.GET_BY_PACKAGE_AND_NAME, query = "select c from Clazz c where "
        + "upper(c.packageName)=:package and upper(c.className)=:name"),
    @NamedQuery(name = ClazzDao.GET_METHOD_NO_SIG, query = "select m from Method m where "
        + "m.clazz.id=:classId and upper(m.methodName)=:name"),
    @NamedQuery(name = ClazzDao.GET_METHOD, query = "select m from Clazz c join c.methods m where "
        + "m.clazz.id=:classId and upper(m.methodName)=:name and (upper(m.shortSignatureTypes)=:params"
        + " or upper(m.shortSignatureStripped)=:params or upper(m.longSignatureTypes)=:params"
        + " or upper(m.longSignatureStripped)=:params)")
})
public class Clazz extends JavadocElement implements Persistent {
    private Long id;
    private Api api;
    private String packageName;
    private String className;
    private Clazz superClass;
    private List<Method> methods;

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
    }

    public final void populate(final ClassDoc doc, final ClazzDao dao) {
        if (doc != null && (methods == null || methods.isEmpty())) {
            final String pkg = doc.containingPackage().name();
            packageName = pkg;
            className = doc.name();
            final ClassDoc superClazz = doc.superclass();
            if (superClazz != null) {
                if (! superClazz.isPackagePrivate()) {
                    superClass = dao.getOrCreate(null, api, superClazz.containingPackage().name(), superClazz.name());
                } else {
                    System.out.println("superClazz = " + superClazz);
                }
            }
            System.out.println("creating class " + this);
            methods = new ArrayList<Method>(doc.methods().length + doc.constructors().length);
            final String path = pkg == null ? "" : pkg.replaceAll("\\.", "/") + "/";
            setLongUrl(getApi().getBaseUrl() + "/" + path + className + ".html");
            dao.save(this);
            for (final MethodDoc methodDoc : doc.methods()) {
                methods.add(new Method(methodDoc, this));
            }
            for (final ConstructorDoc conDoc : doc.constructors()) {
                methods.add(new Method(conDoc, this));
            }
            Collections.sort(methods, new MethodComparator());
        }
    }

    @ManyToOne
    public Api getApi() {
        return api;
    }

    public void setApi(final Api api) {
        this.api = api;
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

    @ManyToOne(fetch = FetchType.LAZY)
    public Clazz getSuperClass() {
        return superClass;
    }

    public void setSuperClass(final Clazz aClass) {
        superClass = aClass;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clazz")
    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(final List<Method> list) {
        methods = list;
    }

    private static class MethodComparator implements Comparator<Method> {
        public int compare(final Method o1, final Method o2) {
            int compare = o1.getMethodName().compareTo(o2.getMethodName());
            if (compare == 0) {
                compare = o1.getParamCount().compareTo(o2.getParamCount());
            }
            return compare;
        }
    }

    @Override
    public String toString() {
        return packageName + "." + className;
    }
}
