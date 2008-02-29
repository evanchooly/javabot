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
    @NamedQuery(name = ClazzDao.DELETE_ALL_METHODS, query = "delete from Method"),
    @NamedQuery(name = ClazzDao.GET_BY_NAME, query = "select c from Clazz c where "
        + "upper(className)=:name"),
    @NamedQuery(name = ClazzDao.GET_BY_PACKAGE_AND_NAME, query = "select c from Clazz c where "
        + "upper(packageName)=:package and upper(className)=:name"),
    @NamedQuery(name = ClazzDao.GET_METHOD_NO_SIG, query = "select m from Method m where "
        + "m.clazz.id=:classId and upper(m.methodName)=:name"),
    @NamedQuery(name = ClazzDao.GET_METHOD, query = "select m from Method m where "
        + "m.clazz.id=:classId and upper(m.methodName)=:name and (upper(shortSignatureTypes)=:params"
        + " or upper(shortSignatureStripped)=:params or upper(longSignatureTypes)=:params"
        + " or upper(longSignatureStripped)=:params)")
})
public class Clazz implements Persistent {
    private static final Logger log = LoggerFactory.getLogger(Clazz.class);

    private Long id;
    private String api;
    private String packageName;
    private String className;
    private String superClass;
    private List<Method> methods;

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

    public Clazz(ClassDoc doc) {
        packageName = doc.containingPackage().name();
        className = doc.name();
        if(doc.superclass() != null) {
            superClass = doc.superclass().qualifiedName();
        }
        System.out.println("creating class " + this);
        methods = new ArrayList<Method>(doc.methods().length + doc.constructors().length);
        for(MethodDoc methodDoc : doc.methods()) {
            methods.add(new Method(methodDoc, this));
        }
        for(ConstructorDoc conDoc : doc.constructors()) {
            methods.add(new Method(conDoc, this));
        }
        Collections.sort(methods, new MethodComparator());
    }

    @Column(nullable = false)
    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    @Transient
    private String getWildcardMethodUrl(String methodName, String baseUrl) {
        for(Method method : methods) {
            if(method.getMethodName().equalsIgnoreCase(methodName)) {
                return method.getMethodUrl(baseUrl);
            }
        }
        return null;
    }

    @Transient
    public String getClassUrl(String baseUrl) {
        return getQualifiedName() + ": " + getClassHTMLPage(baseUrl);
    }

    public String getClassHTMLPage(String baseUrl) {
        String path = packageName == null ? "" : packageName.replaceAll("\\.", "/") + "/";
        return baseUrl + path + className + ".html";
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

    @Transient
    public String getQualifiedName() {
        return (packageName == null ? "" : packageName + ".") + className;
    }

    public String getSuperClass() {
        return superClass;
    }

    public void setSuperClass(String aClass) {
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
