package javabot.dao.impl;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import javabot.dao.AbstractDaoImpl;
import javabot.dao.ClazzDao;
import javabot.javadoc.Clazz;
import javabot.javadoc.Method;

/**
 * Created Jul 27, 2007
 *
 * @author <a href="mailto:javabot@cheeseronline.org">cheeser</a>
 */
public class ClazzDaoImpl extends AbstractDaoImpl<Clazz> implements ClazzDao {
    protected ClazzDaoImpl() {
        super(Clazz.class);
    }

    @SuppressWarnings({"unchecked"})
    public List<Clazz> findAll(final String api) {
        final EntityManager em = getEntityManager();
        return em.createQuery("select c from Clazz c where c.api.name=:api order by c.packageName, c.className")
            .setParameter("api", api)
            .getResultList();
    }

    @SuppressWarnings({"unchecked"})
    public void deleteAll(final String api) {
        final EntityManager em = getEntityManager();
        em.createQuery("delete from Clazz c where c.api.name=:api")
            .setParameter("api", api)
            .executeUpdate();
        getEntityManager().flush();
    }

    @SuppressWarnings({"unchecked"})
    public Clazz[] getClass(final String name) {
        final Query query;
        if (!name.contains(".")) {
            query = getEntityManager().createNamedQuery(ClazzDao.GET_BY_NAME);
            query.setParameter("name", name.toUpperCase());
        } else {
            final String className = name.substring(name.lastIndexOf(".") + 1).toUpperCase();
            final String pkgName = name.substring(0, name.lastIndexOf(".")).toUpperCase();
            query = getEntityManager().createNamedQuery(ClazzDao.GET_BY_PACKAGE_AND_NAME);
            query.setParameter("name", className);
            query.setParameter("package", pkgName);
        }
        final List list = query.getResultList();
        return (Clazz[]) list.toArray(new Clazz[list.size()]);
    }

    @SuppressWarnings({"unchecked"})
    public Clazz[] getClass(final String pkg, final String name) {
        final Query query = getEntityManager().createNamedQuery(ClazzDao.GET_BY_PACKAGE_AND_NAME);
        query.setParameter("name", name);
        query.setParameter("package", pkg);
        final List list = query.getResultList();
        return (Clazz[]) list.toArray(new Clazz[list.size()]);
    }

    @SuppressWarnings({"unchecked"})
    public List<Method> getMethods(final String className, final String methodName, final String signatureTypes) {
        final Clazz[] classes = getClass(className);
        final List<Method> methods = new ArrayList<Method>();
        for (final Clazz clazz : classes) {
            methods.addAll(getMethods(methodName, signatureTypes, clazz));
        }
        return methods;
    }

    private List getMethods(final String name, final String signatureTypes, final Clazz clazz) {
        final Query query;
        if ("*".equals(signatureTypes)) {
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
