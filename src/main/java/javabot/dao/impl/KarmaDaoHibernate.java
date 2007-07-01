package javabot.dao.impl;

import java.util.Date;
import java.util.List;

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
        if(qp.hasSort()) {
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

    public void updateKarma(Karma toUpdate) {
        Karma karma = getKarma(toUpdate.getName());
        karma.setUpdated(new Date());
        changeDao.logChange(karma.getUserName() + " changed '" + karma.getName() + "' to '" + karma.getValue() + "'");
        save(karma);
    }

    public boolean hasKarma(String key) {
        return getKarma(key) != null;
    }

    public void addKarma(String sender, String key, Integer value) {
        Karma karma = new Karma();
        karma.setName(key);
        karma.setValue(value);
        karma.setUserName(sender);
        karma.setUpdated(new Date());
        save(karma);
    }

    public Karma getKarma(String name) {
        return (Karma)getEntityManager().createNamedQuery(KarmaDao.BY_NAME)
            .setParameter("name", name)
            .getSingleResult();
    }

    public Karma get(Long id) {
        return load(id);
    }

    public Long getCount() {
        return (Long)getEntityManager().createQuery(KarmaDao.COUNT).getSingleResult();
    }
}