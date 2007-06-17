package javabot.dao;

import javabot.dao.model.Karma;
import javabot.dao.util.QueryParam;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

// User: joed
// Date: Apr 11, 2007
// Time: 2:41:22 PM

public class KarmaDaoHibernate extends AbstractDaoHibernate<Karma> implements KarmaDao {

    private static final Log log = LogFactory.getLog(KarmaDaoHibernate.class);

    public KarmaDaoHibernate() {
        super(Karma.class);

    }

    @SuppressWarnings("unchecked")
    public Iterator<Karma> getKarmas(QueryParam qp) {
        StringBuilder query = new StringBuilder("from Karma");

        if (qp.hasSort()) {
            query.append(" order by ")
                    .append(qp.getSort())
                    .append((qp.isSortAsc()) ? " desc" : " asc");
        }

        return getSession().createQuery(query.toString())
                .setFirstResult(qp.getFirst())
                .setMaxResults(qp.getCount()).iterate();
    }

    @SuppressWarnings({"unchecked"})
    public Iterator<Karma> getIterator() {

        String query = "from Karma m";

        List<Karma> m_karma = getSession().createQuery(query).list();

        Collections.sort(m_karma, new Comparator<Karma>() {
            public int compare(Karma karma, Karma karma1) {
                return karma.getName().compareTo(karma1.getName());
            }
        });

        return m_karma.iterator();
    }

    public void updateKarma(Karma karma, ChangesDao c_dao) {

        if (hasKarma(karma.getName())) {
            karma.setUpdated(new Date());
            Session session = getSession();
            Transaction transaction = session.beginTransaction();
            session.update(karma);
            transaction.commit();

            c_dao.logChange(karma.getUserName() + " changed '" + karma.getName() + "' to '" + karma.getValue() + "'");
        } else {

            karma.setUpdated(new Date());
            Session session = getSession();
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(karma);
            transaction.commit();

            c_dao.logChange(karma.getUserName() + " changed '" + karma.getName() + "' to '" + karma.getValue() + "'");

        }


    }

    public boolean hasKarma(String key) {
        return getKarma(key).getName() != null;
    }

    public void addKarma(String sender, String key, Integer value) {

        Karma karma = new Karma();

        karma.setName(key);
        karma.setValue(value);
        karma.setUserName(sender);
        karma.setUpdated(new Date());

        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.save(karma);
        transaction.commit();
    }

    public Karma getKarma(String name) {
        String query = "from Karma m where m.name = :name";

        Karma m_karma = (Karma) getSession().createQuery(query)
                .setString("name", name)
                .setMaxResults(1)
                .uniqueResult();

        if (m_karma == null) {

            return new Karma();
        }

        return m_karma;

    }

    public Karma get(Long id) {
        String query = "from Karma m where m.id = :id";

        Karma karma = (Karma) getSession().createQuery(query)
                .setLong("id", id)
                .setMaxResults(1)
                .uniqueResult();

        if (karma == null) {

            return new Karma();
        }

        return karma;

    }

    public Long getCount() {
        String query = "select count(*) from Karma";
        return (Long) getSession().createQuery(query).uniqueResult();
    }
}