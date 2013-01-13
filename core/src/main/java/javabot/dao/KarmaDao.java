package javabot.dao;

import java.util.Date;
import java.util.List;

import javabot.dao.util.QueryParam;
import javabot.model.Karma;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KarmaDao extends BaseDao<Karma> {
    String ALL = "Karma.all";
    String COUNT = "Karma.count";
    String BY_NAME = "Karma.byName";

    private static final Logger log = LoggerFactory.getLogger(KarmaDao.class);

//    @Autowired
    private ChangeDao changeDao;

    public KarmaDao() {
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
        return null;//getEntityManager().createQuery(query.toString())
//                .setFirstResult(qp.getFirst())
//                .setMaxResults(qp.getCount()).getResultList();
    }

    @SuppressWarnings({"unchecked"})
    public List<Karma> findAll() {
        return null;//getEntityManager().createNamedQuery(KarmaDao.ALL).getResultList();
    }

    public void save(Karma karma) {
        karma.setUpdated(new Date());
        if(karma.getId() == null) {
            super.save(karma);
        } else {
            save(karma);
        }
        changeDao.logChange(karma.getUserName() + " changed '" + karma.getName() + "' to '" + karma.getValue() + "'");
    }

    public Karma find(String name) {
        Karma karma = null;
//            karma = (Karma)getEntityManager().createNamedQuery(KarmaDao.BY_NAME)
//                .setParameter("name", name.toLowerCase())
//                .getSingleResult();
        return karma;
    }

    public Long getCount() {
        return null;//(Long) getEntityManager().createQuery("select count(k) from Karma k").getSingleResult();
    }
}