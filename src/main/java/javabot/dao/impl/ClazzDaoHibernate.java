package javabot.dao.impl;

import java.util.List;
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

    public void deleteAll() {
        getEntityManager().createNamedQuery(ClazzDao.DELETE_ALL_METHODS).executeUpdate();
        getEntityManager().createNamedQuery(ClazzDao.DELETE_ALL).executeUpdate();
        getEntityManager().flush();
    }

    public Clazz getOrCreate(ClassDoc classDoc) {
        Clazz clazz;
        Class<? extends ClassDoc> aClass = classDoc.getClass();
        String pkg = aClass.getPackage().getName();
        String className = aClass.getSimpleName();
        try {
            clazz = (Clazz)getEntityManager().createNamedQuery(ClazzDao.GET_BY_PACKAGE_AND_NAME)
                .setParameter("package", pkg)
                .setParameter("name", className)
                .getSingleResult();
        } catch(NoResultException e) {
            clazz = new Clazz(classDoc);
        }
        return clazz;
    }

    @SuppressWarnings({"unchecked", "ToArrayCallWithZeroLengthArrayArgument"})
    public Clazz[] getClass(String name) {
        Query query;
        if(name.indexOf(".") == 0) {
            query = getEntityManager().createNamedQuery(ClazzDao.GET_BY_NAME);
            query.setParameter("name", name);
        } else {
            query = getEntityManager().createNamedQuery(ClazzDao.GET_BY_NAME);
            query.setParameter("name", name.substring(name.lastIndexOf(".") + 1));
            query.setParameter("package", name.substring(0, name.lastIndexOf(".") - 1));
        }
        return (Clazz[])query.getResultList().toArray(new Clazz[0]);
    }

    public List<Method> getMethods(String className, String methodName, String signatureTypes, String baseUrl) {
        return null;
    }
}
