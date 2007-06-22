package javabot.dao.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javabot.model.Factoid;
import javabot.dao.util.QueryParam;
import javabot.dao.AbstractDaoHibernate;
import javabot.dao.FactoidDao;
import javabot.dao.ChangesDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

// User: joed
// Date: Apr 11, 2007
// Time: 2:41:22 PM

public class FactoidDaoHibernate extends AbstractDaoHibernate<Factoid> implements FactoidDao {
    private static final Log log = LogFactory.getLog(FactoidDaoHibernate.class);

    public FactoidDaoHibernate() {
        super(Factoid.class);

    }

    @SuppressWarnings("unchecked")
    public Iterator<Factoid> getFactoids(QueryParam qp) {
        StringBuilder query = new StringBuilder("from Factoid f where f.name not like 'karma%'");

        if (qp.hasSort()) {
            query.append(" order by ")
                    .append(qp.getSort())
                    .append((qp.isSortAsc()) ? " asc" : " desc");
        }

        return getSession().createQuery(query.toString())
                .setFirstResult(qp.getFirst())
                .setMaxResults(qp.getCount()).iterate();
    }

    @SuppressWarnings({"unchecked"})
    public List<Factoid> getFactoids() {

        String query = "from Factoid m";

        List<Factoid> m_factoids = getSession().createQuery(query).list();

        Collections.sort(m_factoids, new Comparator<Factoid>() {
            public int compare(Factoid factoid, Factoid factoid1) {
                return factoid.getName().compareTo(factoid1.getName());
            }
        });

        return m_factoids;
    }

    @SuppressWarnings({"unchecked"})
    public Iterator<Factoid> getIterator() {

        String query = "from Factoid m";

        List<Factoid> m_factoids = getSession().createQuery(query).list();

        Collections.sort(m_factoids, new Comparator<Factoid>() {
            public int compare(Factoid factoid, Factoid factoid1) {
                return factoid.getName().compareTo(factoid1.getName());
            }
        });

        return m_factoids.iterator();
    }

    public void updateFactoid(Factoid factoid, ChangesDao changesDao) {

        factoid.setUpdated(new Date());
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.update(factoid);
        transaction.commit();

        changesDao.logChange(factoid.getUserName() + " changed '" + factoid.getName() + "' to '" + factoid.getValue() + "'");
    }

    public boolean hasFactoid(String key) {
        return getFactoid(key).getName() != null;
    }

    public void addFactoid(String sender, String key, String value, ChangesDao changesDao) {

        Factoid factoid = new Factoid();

        factoid.setName(key);
        factoid.setValue(value);
        factoid.setUserName(sender);
        factoid.setUpdated(new Date());

        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.save(factoid);
        transaction.commit();
        changesDao.logAdd(sender, key, value);
    }

    public void forgetFactoid(String sender, String key, ChangesDao changesDao) {

        Factoid factoid = getFactoid(key);

        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.delete(factoid);
        transaction.commit();

        changesDao.logChange(sender + " removed '" + key + "'");
    }

    public Factoid getFactoid(String name) {
        String query = "from Factoid m where m.name = :name";

        Factoid m_factoid = (Factoid) getSession().createQuery(query)
                .setString("name", name)
                .setMaxResults(1)
                .uniqueResult();

        if (m_factoid == null) {
            return new Factoid();
        }

        return m_factoid;

    }

    public Factoid get(Long id) {
        String query = "from Factoid m where m.id = :id";

        Factoid m_factoid = (Factoid) getSession().createQuery(query)
                .setLong("id", id)
                .setMaxResults(1)
                .uniqueResult();

        if (m_factoid == null) {
            return new Factoid();
        }

        return m_factoid;

    }

    public Long getNumberOfFactoids() {
        String query = "select count(*) from Factoid f where f.name not like 'karma%'";
        return (Long) getSession().createQuery(query).uniqueResult();

    }

    public Long factoidCountFiltered(Factoid filter) {
        return (Long) buildFindQuery(null, filter, true).uniqueResult();
    }

    @SuppressWarnings({"unchecked"})
    public Iterator<Factoid> getFactoidsFiltered(QueryParam qp, Factoid filter) {

        return (Iterator<Factoid>) buildFindQuery(qp, filter, false).iterate();

    }

    private Query buildFindQuery(QueryParam qp, Factoid filter, boolean count) {
        StringBuffer hql = new StringBuffer();

        if (count) {
            hql.append("select count(*) ");
        }
        hql.append(" from Factoid target where 1=1 and target.name not like 'karma%' ");

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
                    ") ").append((qp.isSortAsc()) ? " asc" : " desc");
        }

        Query query = getSession().createQuery(hql.toString());

        if (filter.getName() != null) {
            query.setParameter("name", "%" + filter.getName().toUpperCase() + "%");
        }
        if (filter.getUserName() != null) {
            query.setParameter("username", "%" + filter.getUserName() + "%");
        }
        if (filter.getValue() != null) {
            query.setParameter("value", "%" + filter.getValue() + "%");
        }

        if (!count && qp != null) {
            query.setFirstResult(qp.getFirst()).setMaxResults(qp.getCount());
        }

        return query;
    }


}
