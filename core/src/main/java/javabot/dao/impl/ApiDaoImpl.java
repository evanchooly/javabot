package javabot.dao.impl;

import javabot.dao.AbstractDaoImpl;
import javabot.dao.ApiDao;
import javabot.javadoc.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * Created Oct 29, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@SuppressWarnings({"unchecked"})
@Component
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
                .setParameter("name", name.toUpperCase())
                .getSingleResult();
        } catch(NoResultException e) {
            // no such API yet
        }
        return api;
    }

    public List<Api> findAll() {
        return (List<Api>)getEntityManager()
            .createNamedQuery(ApiDao.FIND_ALL)
            .getResultList();
    }
}