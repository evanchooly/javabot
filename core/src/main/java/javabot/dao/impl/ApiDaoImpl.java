package javabot.dao.impl;

import java.util.List;
import javax.persistence.NoResultException;

import javabot.dao.AbstractDaoImpl;
import javabot.dao.ApiDao;
import javabot.javadoc.Api;
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
}