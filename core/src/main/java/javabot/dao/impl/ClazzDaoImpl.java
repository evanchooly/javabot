package javabot.dao.impl;

import javabot.dao.AbstractDaoImpl;
import javabot.dao.ClazzDao;
import javabot.javadoc.Clazz;
import javabot.javadoc.Field;
import javabot.javadoc.Method;
import javabot.model.Persistent;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created Jul 27, 2007
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@Component
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
        final String[] strings = calculateNameAndPackage(name);
        final String pkgName = strings[0];
        final String className = strings[1].toUpperCase();
        if (pkgName == null) {
            query = getEntityManager().createNamedQuery(ClazzDao.GET_BY_NAME);
            query.setParameter("name", className);
        } else {
            query = getEntityManager().createNamedQuery(ClazzDao.GET_BY_PACKAGE_AND_NAME);
            query.setParameter("name", className);
            query.setParameter("package", pkgName.toUpperCase());
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
    public List<Field> getField(final String className, final String fieldName) {
        final String[] strings = calculateNameAndPackage(className);
        final String pkgName = strings[0];
        final String clsName = strings[1].toUpperCase();
        Query query;
        if (pkgName == null) {
            query = getEntityManager().createNamedQuery(ClazzDao.GET_FIELD_WITH_CLS);
            query.setParameter("className", clsName);
            query.setParameter("fieldName", fieldName.toUpperCase());
        } else {
            query = getEntityManager().createNamedQuery(ClazzDao.GET_FIELD_WITH_CLS_AND_PKG);
            query.setParameter("className", clsName);
            query.setParameter("packageName", pkgName.toUpperCase());
            query.setParameter("fieldName", fieldName.toUpperCase());
        }
        return (List<Field>) query.getResultList();
    }

    @SuppressWarnings({"unchecked"})
    public List<Method> getMethods(final String className, final String methodName, final String signatureTypes) {
        final Clazz[] classes = getClass(className);
        List<Clazz> list = new ArrayList<Clazz>(Arrays.asList(classes));
        final List<Method> methods = new ArrayList<Method>();
        while (!list.isEmpty()) {
            Clazz clazz = list.remove(0);
            if (clazz != null) {
                final List<Method> m = getMethods(methodName, signatureTypes, clazz);
                methods.addAll(m);
                list.add(clazz.getSuperClass());
            }
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

    @Override
    public void delete(Persistent persistedObject) {
        getEntityManager()
                .createQuery("delete from Method m where m.clazz=:clazz")
                .setParameter("clazz", this)
        .executeUpdate();
        getEntityManager()
                .createQuery("delete from Field f where f.clazz=:clazz")
                .setParameter("clazz", this)
        .executeUpdate();

        super.delete(persistedObject);
    }

    private String[] calculateNameAndPackage(final String href) {
        String clsName = href;
        while (clsName.contains(".") && Character.isLowerCase(clsName.charAt(0))) {
            clsName = clsName.substring(clsName.indexOf(".") + 1);
        }
        String pkgName = href.equals(clsName) ? null : href.substring(0, href.indexOf(clsName) - 1);
        return new String[]{pkgName, clsName};
    }
}
