package javabot.dao.impl;

import java.util.List;
import javax.persistence.NoResultException;

import javabot.dao.AbstractDaoHibernate;
import javabot.dao.ApiDao;
import javabot.javadoc.Api;

/**
 * Created Oct 29, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@SuppressWarnings({"unchecked"})
public class ApiDaoHibernate extends AbstractDaoHibernate<Api> implements ApiDao {
    protected ApiDaoHibernate() {
        super(Api.class);
    }

    public Api find(String name) {
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

    public void delete(String name) {
        Api api = find(name);
        if(api != null) {
            delete(api);
        }
    }
}