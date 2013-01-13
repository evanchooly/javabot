package javabot.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.code.morphia.query.Query;
import javabot.dao.util.QueryParam;
import javabot.model.Config;
import javabot.model.Factoid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings({"ConstantNamingConvention"})
public class FactoidDao extends BaseDao {
    String ALL = "Factoid.all";
    String COUNT = "Factoid.count";
    String BY_NAME = "Factoid.byName";
    String BY_PARAMETERIZED_NAME = "Factoid.byParameterizedName";

    private static final Logger log = LoggerFactory.getLogger(FactoidDao.class);

//    @Autowired
    private ChangeDao changeDao;
    @Autowired
    private ConfigDao dao;

    public FactoidDao() {
        super(Factoid.class);
    }

    public List<Factoid> find(final QueryParam qp) {
        final StringBuilder query = new StringBuilder("from Factoid f");
        if (qp.hasSort()) {
            query.append(" order by ")
                .append(qp.getSort())
                .append(qp.isSortAsc() ? " asc" : " desc");
        }
        return null;//getEntityManager().createQuery(query.toString())
//            .setFirstResult(qp.getFirst())
//            .setMaxResults(qp.getCount()).getResultList();
    }

    public List<Factoid> getFactoids() {
        return null;//(List<Factoid>) getEntityManager().createNamedQuery(ALL).getResultList();
    }

    public void save(final Factoid factoid) {
        super.save(factoid);
        changeDao.logChange(factoid.getUserName() + " changed '" + factoid.getName()
            + "' to '" + factoid.getValue() + "'");
    }

    public boolean hasFactoid(final String key) {
        final List<Factoid> list =null;// getEntityManager().createNamedQuery(FactoidDao.BY_NAME)
//            .setParameter("name", key)
//            .getResultList();
        return !list.isEmpty();
    }

    @Transactional
    public Factoid addFactoid(final String sender, final String key, final String value) {
        final Factoid factoid = new Factoid();
        factoid.setId(factoid.getId());
        factoid.setName(key);
        factoid.setValue(value);
        factoid.setUserName(sender);
        factoid.setUpdated(new Date());
        factoid.setLastUsed(new Date());
        save(factoid);
        changeDao.logAdd(sender, key, value);

        return factoid;
    }

    public void delete(final String sender, final String key) {
        final Factoid factoid = getFactoid(key);
        if (factoid != null) {
            delete(factoid.getId());
            changeDao.logChange(String.format("%s remove '%s' with a value of '%s'", sender, key, factoid.getValue()));
        }
    }

    public Factoid getFactoid(final String name) {
        final List<Factoid> list = null;//getEntityManager().createNamedQuery(FactoidDao.BY_NAME)
//            .setParameter("name", name.toLowerCase())
//            .getResultList();
        Factoid factoid = null;
        if(! list.isEmpty()) {
            factoid = list.get(0);
            factoid.setLastUsed(new Date());
            super.save(factoid);
        }
        return factoid;
    }

    public Factoid getParameterizedFactoid(final String name) {
        final String lower = name.toLowerCase();
        final Query<Factoid> query = null;//getEntityManager().createNamedQuery(FactoidDao.BY_PARAMETERIZED_NAME)
//            .setParameter("name1", lower + " $1")
//            .setParameter("name2", lower + " $^")
//            .setParameter("name3", lower + " $+");
        final List<Factoid> list = query.asList();
        Factoid factoid = null;
        if(! list.isEmpty()) {
            factoid = list.get(0);
            factoid.setLastUsed(new Date());
            super.save(factoid);
        }
        return factoid;
    }

    public Long count() {
        return null;//(Long) getEntityManager().createNamedQuery(FactoidDao.COUNT).getSingleResult();
    }

    public Long factoidCountFiltered(final Factoid filter) {
        return null;//(Long) buildFindQuery(null, filter, true).getSingleResult();
    }

    public List<Factoid> getFactoidsFiltered(final QueryParam qp, final Factoid filter) {
        return null;//buildFindQuery(qp, filter, false).getResultList();
    }

    private Query buildFindQuery(final QueryParam qp, final Factoid filter, final boolean count) {
        final StringBuilder hql = new StringBuilder();
        if (count) {
            hql.append("select count(*) ");
        }
        hql.append(" from Factoid target where 1=1 ");
        if (filter.getName() != null) {
            hql.append("and upper(target.name) like :name ");
        }
        if (filter.getUserName() != null) {
            hql.append("and upper(target.userName) like :username ");
        }
        if (filter.getValue() != null) {
            hql.append("and upper(target.value) like :value ");
        }
        if (!count && qp != null && qp.hasSort()) {
            hql.append("order by upper(target.").append(qp.getSort()).append(
                ") ").append(qp.isSortAsc() ? " asc" : " desc");
        }
        final Query query = null;//getEntityManager().createQuery(hql.toString());
        if (filter.getName() != null) {
//            query.setParameter("name", "%" + filter.getName().toUpperCase() + "%");
        }
        if (filter.getUserName() != null) {
//            query.setParameter("username", "%" + filter.getUserName().toUpperCase() + "%");
        }
        if (filter.getValue() != null) {
//            query.setParameter("value", "%" + filter.getValue().toUpperCase() + "%");
        }
        if (!count && qp != null) {
//            query.setFirstResult(qp.getFirst()).setMaxResults(qp.getCount());
        }
        return query;
    }

//    @Scheduled(cron = "0 0 2 * * ?")
    public void pruneFactoids() {
        log.debug("FactoidDao.pruneFactoids");
        final Calendar cal = Calendar.getInstance();
        final Config config = dao.get();
        final Integer length = config.getHistoryLength();
        if (length != null && length != 0) {
            cal.clear(Calendar.MILLISECOND);
            cal.clear(Calendar.SECOND);
            cal.clear(Calendar.HOUR);
            cal.add(Calendar.MONTH, length * -1);
//            getEntityManager().createQuery("delete from Factoid f where f.lastUsed < :date and f.lastUsed is not null")
//                .setParameter("date", cal.getTime())
//                .executeUpdate();
        }
    }
    }
