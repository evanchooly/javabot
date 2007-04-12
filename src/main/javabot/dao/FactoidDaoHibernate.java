package javabot.dao;

import javabot.dao.model.factoids;
import org.hibernate.SessionFactory;

// User: joed
// Date: Apr 11, 2007
// Time: 2:41:22 PM


public class FactoidDaoHibernate extends AbstractDaoHibernate<factoids> implements FactoidDao {

    public FactoidDaoHibernate() {
        super(factoids.class);
    }

    public factoids load(String name) {
        String query = "from factoids m where m.name = :name";

        return (factoids) getSession().createQuery(query)
                .setString("name", name)
                .setMaxResults(1)
                .uniqueResult();
    }
}
