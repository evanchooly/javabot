package javabot.dao.impl;

import java.util.Date;
import java.util.List;
import javax.persistence.NoResultException;

import javabot.dao.AbstractDaoHibernate;
import javabot.dao.ChangeDao;
import javabot.dao.KarmaDao;
import javabot.dao.util.QueryParam;
import javabot.model.Karma;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class KarmaDaoHibernate extends AbstractDaoHibernate<Karma> implements KarmaDao {
    private static final Log log = LogFactory.getLog(KarmaDaoHibernate.class);
    private ChangeDao changeDao;

    public KarmaDaoHibernate() {
        super(Karma.class);

    }

    @SuppressWarnings("unchecked")
    public List<Karma> getKarmas(QueryParam qp) {
        StringBuilder query = new StringBuilder("from Karma");
        if (qp.hasSort()) {
            query.append(" order by ")
                    .append(qp.getSort())
                    .append((qp.isSortAsc()) ? " desc" : " asc");
        }
        return getEntityManager().createQuery(query.toString())
                .setFirstResult(qp.getFirst())
                .setMaxResults(qp.getCount()).getResultList();
    }

    @SuppressWarnings({"unchecked"})
    public List<Karma> findAll() {
        return getEntityManager().createNamedQuery(KarmaDao.ALL).getResultList();
    }

    @Override
    public void save(Karma karma) {
        karma.setUpdated(new Date());
        super.save(karma);
        changeDao.logChange(karma.getUserName() + " changed '" + karma.getName() + "' to '" + karma.getValue() + "'");
    }

    public Karma find(String name) {
        Karma karma = null;
        try {
            karma = (Karma)getEntityManager().createNamedQuery(KarmaDao.BY_NAME)
                .setParameter("name", name)
                .getSingleResult();
        } catch(NoResultException e) {
        }
        return karma;
    }

    public Long getCount() {
        return (Long) getEntityManager().createQuery("select count(k) from Karma k").getSingleResult();
    }

    public void setChangeDao(ChangeDao dao) {
        this.changeDao = dao;
    }
}