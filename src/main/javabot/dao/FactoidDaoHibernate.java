package javabot.dao;

import javabot.dao.model.Factoids;
import org.hibernate.SessionFactory;

// User: joed
// Date: Apr 11, 2007
// Time: 2:41:22 PM


public class FactoidDaoHibernate extends AbstractDaoHibernate<Factoids> implements FactoidDao {

    public FactoidDaoHibernate() {
        super(Factoids.class);
    }

    public Factoids load(String name) {
        String query = "from Factoids m where m.name = :name";

        return (Factoids) getSession().createQuery(query)
                .setString("name", name)
                .setMaxResults(1)
                .uniqueResult();
    }
}
