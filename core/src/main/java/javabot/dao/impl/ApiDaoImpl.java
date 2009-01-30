package javabot.dao.impl;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;

import javabot.dao.AbstractDaoImpl;
import javabot.dao.ApiDao;
import javabot.javadoc.Api;
import javabot.javadoc.Clazz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created Oct 29, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@SuppressWarnings({"unchecked"})
public class ApiDaoImpl extends AbstractDaoImpl<Api> implements ApiDao {
    private static final Logger log = LoggerFactory.getLogger(ApiDaoImpl.class);
    protected ApiDaoImpl() {
        super(Api.class);
    }

    public Api find(final String name) {
        Api api = null;
        try {
            api = (Api)getEntityManager()
                .createNamedQuery(ApiDao.FIND_BY_NAME)
                .setParameter("name", name)
                .getSingleResult();
        } catch(NoResultException e) {
            // no such API yet
        }
        return api;
    }

    public List<String> listNames() {
        return (List<String>)getEntityManager()
            .createNamedQuery(ApiDao.LIST_NAMES)
            .getResultList();
    }

    public void delete(final Api api) {
        if(api != null) {
            final Api old = find(api.getName());
            final List<Clazz> classes = old.getClasses();
            for(int passes = 0; passes < 5 && !classes.isEmpty(); passes++) {
                final List<Clazz> skipped = new ArrayList<Clazz>();
                while(!classes.isEmpty()) {
                    final Clazz aClass = classes.remove(classes.size()-1);
                    try {
                        delete(aClass);
                    } catch (Exception e) {
                        log.debug("rescheduling" + aClass + " : " + e.getMessage());
                        skipped.add(aClass);
                    }
                }
                classes.addAll(skipped);
            }
            super.delete(old);
        }
    }
}