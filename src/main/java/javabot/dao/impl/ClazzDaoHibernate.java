package javabot.dao.impl;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.sun.javadoc.ClassDoc;
import javabot.dao.AbstractDaoHibernate;
import javabot.dao.ClazzDao;
import javabot.javadoc.Api;
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

    @SuppressWarnings({"unchecked"})
    public void deleteAll(String api) {
        EntityManager em = getEntityManager();
        em.createQuery("delete from Clazz c where c.api.name=:api")
            .setParameter("api", api)
            .executeUpdate();
        getEntityManager().flush();
    }

    public Clazz getOrCreate(ClassDoc classDoc, Api api, String packageName, String name) {
        Clazz clazz;
        String pkg = classDoc == null ?  packageName : classDoc.containingPackage().name();
        String className = classDoc == null ? name : classDoc.name();
        try {
            clazz = (Clazz)getEntityManager().createNamedQuery(ClazzDao.GET_BY_PACKAGE_AND_NAME)
                .setParameter("api", api)
                .setParameter("package", packageName)
                .setParameter("name", name.toUpperCase())
                .getSingleResult();
        } catch(NoResultException e) {
            clazz = new Clazz(api, pkg, name);
            save(clazz);
        }
        clazz.populate(classDoc, this);
        return clazz;
    }

    @SuppressWarnings({"unchecked"})
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
        List list = query.getResultList();
        return (Clazz[])list.toArray(new Clazz[list.size()]);
    }

    @SuppressWarnings({"unchecked"})
    public List<Method> getMethods(String className, String methodName, String signatureTypes) {
        Clazz[] classes = getClass(className);
        List<Method> methods = new ArrayList<Method>();
        for(Clazz clazz : classes) {
            methods.addAll(getMethods(methodName, signatureTypes, clazz));
        }
        return methods;
    }

    private List getMethods(String name, String signatureTypes, Clazz clazz) {
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
