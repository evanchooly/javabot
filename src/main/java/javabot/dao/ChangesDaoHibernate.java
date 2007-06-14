package javabot.dao;

import javabot.dao.model.Change;
import javabot.dao.model.Factoid;
import javabot.dao.util.QueryParam;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.Iterator;

// User: joed
// Date: Apr 11, 2007
// Time: 2:41:22 PM


public class ChangesDaoHibernate extends AbstractDaoHibernate<Factoid> implements ChangesDao {

    public ChangesDaoHibernate() {
        super(Change.class);
    }

    @SuppressWarnings("unchecked")
    public Iterator<Change> getChanges(QueryParam qp) {
        StringBuilder query = new StringBuilder("from Change f ");

        if (qp.hasSort()) {
            query.append(" order by ")
                    .append(qp.getSort())
                    .append((qp.isSortAsc()) ? " asc" : " desc");
        }

        return getSession().createQuery(query.toString())
                .setFirstResult(qp.getFirst())
                .setMaxResults(qp.getCount()).iterate();
    }

    public void logChange(String message) {

        Change change = new Change();

        change.setMessage(message);
        change.setChangeDate(new Date());

        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        session.save(change);
        transaction.commit();
    }

    public void logAdd(String sender, String key, String value) {
        logChange(sender + " added '" + key + "' with a value of '" + value + "'");
    }

    public boolean findLog(String message) {
        boolean found = false;

        //            SELECT * FROM Change WHERE message=?

        String query = "from Change c where c.message = :message";
        Change change = (Change) getSession().createQuery(query)
                .setString("message", message)
                .uniqueResult();

        if (!(change == null)) {
            found = true;
        }

        return found;
    }

    public Long getNumberOfChanges() {
        String query = "select count(*) from Change c";
        return (Long) getSession().createQuery(query).uniqueResult();

    }

    public Change get(Long id) {
        String query = "from Change m where m.id = :id";

        Change change = (Change) getSession().createQuery(query)
                .setLong("id", id)
                .setMaxResults(1)
                .uniqueResult();

        if (change == null) {

            return new Change();
        }

        return change;

    }

}