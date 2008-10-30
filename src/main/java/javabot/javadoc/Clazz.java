package javabot.javadoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    @NamedQuery(name = ClazzDao.GET_BY_PACKAGE_AND_NAME, query = "select c from Clazz c where "
        + "c.packageName=:package and c.api=:api and upper(c.className)=:name"),
    @NamedQuery(name = ClazzDao.GET_METHOD_NO_SIG, query = "select m from Method m where "
        + "m.clazz.id=:classId and upper(m.methodName)=:name"),
    @NamedQuery(name = ClazzDao.GET_METHOD, query = "select m from Clazz c join c.methods m where "
        + "m.clazz.id=:classId and upper(m.methodName)=:name and (upper(m.shortSignatureTypes)=:params"
        + " or upper(m.shortSignatureStripped)=:params or upper(m.longSignatureTypes)=:params"
        + " or upper(m.longSignatureStripped)=:params)")
})
public class Clazz implements Persistent {
    private static final Logger log = LoggerFactory.getLogger(Clazz.class);
    
    private Long id;
    private Api api;
    private String packageName;
    private String className;
    private Clazz superClass;
    private List<Method> methods;
    private String classUrl;

    public Clazz() {
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long classId) {
        id = classId;
    }

    public Clazz(Api apiName, String pkg, String name) {
        api = apiName;
        packageName = pkg;
        className = name;
    }

    public final void populate(ClassDoc doc, ClazzDao dao) {
        if(doc != null && (methods == null || methods.isEmpty())) {
            String pkg = doc.containingPackage().name();
            packageName = pkg;
            className = doc.name();
            ClassDoc superClazz = doc.superclass();
            if(superClazz != null) {
                superClass = dao.getOrCreate(null, api, superClazz.containingPackage().name(), superClazz.name());
            }
            System.out.println("creating class " + this);
            methods = new ArrayList<Method>(doc.methods().length + doc.constructors().length);
            String path = pkg == null ? "" : pkg.replaceAll("\\.", "/") + "/";
            classUrl = getApi().getBaseUrl() + "/" + path + className + ".html";
            dao.save(this);
            for(MethodDoc methodDoc : doc.methods()) {
                methods.add(new Method(methodDoc, this));
            }
            for(ConstructorDoc conDoc : doc.constructors()) {
                methods.add(new Method(conDoc, this));
            }
            Collections.sort(methods, new MethodComparator());
        }
    }

    @ManyToOne
    public Api getApi() {
        return api;
    }

    public void setApi(Api api) {
        this.api = api;
    }

    @Transient
    private String getWildcardMethodUrl(String methodName) {
        for(Method method : methods) {
            if(method.getMethodName().equalsIgnoreCase(methodName)) {
                return method.getMethodUrl();
            }
        }
        return null;
    }

    @Column(length = 1000)
    public String getClassUrl() {
        return classUrl;
    }

    public void setClassUrl(String classUrl) {
        this.classUrl = classUrl;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String name) {
        packageName = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String name) {
        className = name;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Clazz getSuperClass() {
        return superClass;
    }

    public void setSuperClass(Clazz aClass) {
        superClass = aClass;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy="clazz")
    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(List<Method> list) {
        methods = list;
    }

    private static class MethodComparator implements Comparator<Method> {
        public int compare(Method o1, Method o2) {
            int compare = o1.getMethodName().compareTo(o2.getMethodName());
            if(compare == 0) {
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
