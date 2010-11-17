package javabot.dao.impl;

import java.util.Date;
import java.util.List;
import javax.persistence.NoResultException;

import javabot.dao.AbstractDaoImpl;
import javabot.dao.ChangeDao;
import javabot.dao.KarmaDao;
import javabot.dao.util.QueryParam;
import javabot.model.Karma;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KarmaDaoImpl extends AbstractDaoImpl<Karma> implements KarmaDao {
    private static final Logger log = LoggerFactory.getLogger(KarmaDaoImpl.class);

    @Autowired
    private ChangeDao changeDao;

    public KarmaDaoImpl() {
        super(Karma.class);

    }

    @SuppressWarnings("unchecked")
    public List<Karma> getKarmas(QueryParam qp) {
        StringBuilder query = new StringBuilder("from Karma");
        if (qp.hasSort()) {
            query.append(" order by ")
                    .append(qp.getSort())
                    .append(qp.isSortAsc() ? " desc" : " asc");
        }
        return getEntityManager().createQuery(query.toString())
                .setFirstResult(qp.getFirst())
                .setMaxResults(qp.getCount()).getResultList();
    }

    @SuppressWarnings({"unchecked"})
    public List<Karma> findAll() {
        return getEntityManager().createNamedQuery(KarmaDao.ALL).getResultList();
    }

    public void save(Karma karma) {
        karma.setUpdated(new Date());
        if(karma.getId() == null) {
            super.save(karma);
        } else {
            merge(karma);
        }
        changeDao.logChange(karma.getUserName() + " changed '" + karma.getName() + "' to '" + karma.getValue() + "'");
    }

    public Karma find(String name) {
        Karma karma = null;
        try {
            karma = (Karma)getEntityManager().createNamedQuery(KarmaDao.BY_NAME)
                .setParameter("name", name.toLowerCase())
                .getSingleResult();
        } catch(NoResultException e) {
        }
        return karma;
    }

    public Long getCount() {
        return (Long) getEntityManager().createQuery("select count(k) from Karma k").getSingleResult();
    }
}