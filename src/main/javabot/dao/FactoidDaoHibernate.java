package javabot.dao;

import javabot.dao.model.factoids;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;

// User: joed
// Date: Apr 11, 2007
// Time: 2:41:22 PM


public class FactoidDaoHibernate extends AbstractDaoHibernate<factoids> implements FactoidDao {

    private Properties _properties;

    private HtmlRoutines html = new HtmlRoutines();

    private static final Log log = LogFactory.getLog(FactoidDaoHibernate.class);

    public FactoidDaoHibernate() {
        super(factoids.class);


    }


    @SuppressWarnings({"unchecked"})
    public List<factoids> getFactoids() {

        String query = "from factoids m";

        List<factoids> m_factoids = getSession().createQuery(query).list();

        Collections.sort(m_factoids, new Comparator<factoids>() {
            public int compare(factoids factoid, factoids factoid1) {
                return factoid.getName().compareTo(factoid1.getName());
            }
        });

        return m_factoids;
    }

    public void updateFactoid(factoids factoid, ChangesDao c_dao, String htmlFile) {

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

        factoids factoid = new factoids();

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

        factoids factoid = getFactoid(key);

        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.delete(factoid);
        transaction.commit();


        html.dumpHTML(getFactoids(), htmlFile);
        c_dao.logChange(sender + " removed '" + key + "'");
    }

    public factoids getFactoid(String name) {
        String query = "from factoids m where m.name = :name";

        factoids m_factoid = (factoids) getSession().createQuery(query)
                .setString("name", name)
                .setMaxResults(1)
                .uniqueResult();

        if (m_factoid == null) {

            factoids notFound = new factoids();
            return notFound;
        }

        return m_factoid;

    }

    public Long getNumberOfFactoids() {
        String query = "select count(*) from factoids";
        return (Long) getSession().createQuery(query).uniqueResult();

    }
}
