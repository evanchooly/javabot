package javabot.dao.impl;

import java.util.List;
import java.util.ArrayList;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.sun.javadoc.ClassDoc;
import javabot.dao.AbstractDaoHibernate;
import javabot.dao.ClazzDao;
import javabot.javadoc.Clazz;
import javabot.javadoc.Method;

/**
 * Created Jul 27, 2007
 *
 * @author <a href="mailto:javabot@cheeseronline.org">cheeser</a>
 */
public class ClazzDaoHibernate extends AbstractDaoHibernate<Clazz> implements ClazzDao {
    protected ClazzDaoHibernate() {
        super(Clazz.class);
    }

    public void deleteAll(String pkgName) {
//        getEntityManager()
//            .createNamedQuery(ClazzDao.DELETE_ALL_METHODS)
//            .setParameter("api", pkgName)
//            .executeUpdate();
        getEntityManager()
            .createNamedQuery(ClazzDao.DELETE_ALL)
            .setParameter("api", pkgName)
            .executeUpdate();
        getEntityManager().flush();
    }

    public Clazz getOrCreate(ClassDoc classDoc) {
        Clazz clazz;
        Class<? extends ClassDoc> aClass = classDoc.getClass();
        String pkg = aClass.getPackage().getName();
        String className = aClass.getSimpleName();
        try {
            clazz = (Clazz)getEntityManager().createNamedQuery(ClazzDao.GET_BY_PACKAGE_AND_NAME)
                .setParameter("package", pkg.toUpperCase())
                .setParameter("name", className.toUpperCase())
                .getSingleResult();
        } catch(NoResultException e) {
            clazz = new Clazz(classDoc);
        }
        return clazz;
    }

    @SuppressWarnings({"unchecked", "ToArrayCallWithZeroLengthArrayArgument"})
    public Clazz[] getClass(String name) {
        Query query;
        if(!name.contains(".")) {
            query = getEntityManager().createNamedQuery(ClazzDao.GET_BY_NAME);
            query.setParameter("name", name.toUpperCase());
        } else {
            query = getEntityManager().createNamedQuery(ClazzDao.GET_BY_PACKAGE_AND_NAME);
            query.setParameter("name", name.substring(name.lastIndexOf(".") + 1).toUpperCase());
            query.setParameter("package", name.substring(0, name.lastIndexOf(".")).toUpperCase());
        }
        return (Clazz[])query.getResultList().toArray(new Clazz[0]);
    }

    @SuppressWarnings({"unchecked"})
    public List<Method> getMethods(String className, String methodName, String signatureTypes, String baseUrl) {
        Clazz[] classes = getClass(className);
        List<Method> methods = new ArrayList<Method>();
        for(Clazz clazz : classes) {
            methods.addAll(getMethods(methodName, signatureTypes, clazz));
        }
        return methods;
    }

    private List getMethods(String name, String signatureTypes, Clazz clazz) {
        List methods = new ArrayList<Method>();
        Query query;
        if("*".equals(signatureTypes)) {
            query = getEntityManager().createNamedQuery(ClazzDao.GET_METHOD_NO_SIG)
                .setParameter("classId", clazz.getId())
                .setParameter("name", name.toUpperCase());
        } else {
            query = getEntityManager().createNamedQuery(ClazzDao.GET_METHOD)
                .setParameter("classId", clazz.getId())
                .setParameter("name", name.toUpperCase())
                .setParameter("params", signatureTypes.toUpperCase());
        }
        return query.getResultList();
    }
}
