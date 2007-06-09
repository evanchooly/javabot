package javabot.dao;

import javabot.dao.model.Factoid;
import javabot.dao.util.QueryParam;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.*;

// User: joed
// Date: Apr 11, 2007
// Time: 2:41:22 PM


public class FactoidDaoHibernate extends AbstractDaoHibernate<Factoid> implements FactoidDao {

    private HtmlRoutines html = new HtmlRoutines();

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

    @SuppressWarnings("unchecked")
    public Iterator<Factoid> getKarmas(QueryParam qp) {
        StringBuilder query = new StringBuilder("from Factoid f where f.name like 'karma%'");

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

    public void updateFactoid(Factoid factoid, ChangesDao c_dao, String htmlFile) {

        factoid.setUpdated(new Date());
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.update(factoid);
        transaction.commit();

        c_dao.logChange(factoid.getUserName() + " changed '" + factoid.getName() + "' to '" + factoid.getValue() + "'");
        html.dumpHTML(getFactoids(), htmlFile);

    }

    public boolean hasFactoid(String key) {
        return getFactoid(key).getName() != null;
    }

    public void addFactoid(String sender, String key, String value, ChangesDao c_dao, String htmlFile) {

        Factoid factoid = new Factoid();

        factoid.setName(key);
        factoid.setValue(value);
        factoid.setUserName(sender);
        factoid.setUpdated(new Date());

        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.save(factoid);
        transaction.commit();
        c_dao.logAdd(sender, key, value);

        html.dumpHTML(getFactoids(), htmlFile);

    }

    public void forgetFactoid(String sender, String key, ChangesDao c_dao, String htmlFile) {

        Factoid factoid = getFactoid(key);

        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.delete(factoid);
        transaction.commit();


        html.dumpHTML(getFactoids(), htmlFile);
        c_dao.logChange(sender + " removed '" + key + "'");
    }

    public Factoid getFactoid(String name) {
        String query = "from Factoid m where m.name = :name";

        Factoid m_factoid = (Factoid) getSession().createQuery(query)
                .setString("name", name)
                .setMaxResults(1)
                .uniqueResult();

        if (m_factoid == null) {

            Factoid notFound = new Factoid();
            return notFound;
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

            Factoid notFound = new Factoid();
            return notFound;
        }

        return m_factoid;

    }

    public Long getNumberOfFactoids() {
        String query = "select count(*) from Factoid f where f.name not like 'karma%'";
        return (Long) getSession().createQuery(query).uniqueResult();

    }


    public Long getNumberOfKarmas() {
        String query = "select count(*) from Factoid f where f.name like 'karma%'";
        return (Long) getSession().createQuery(query).uniqueResult();

    }
}
