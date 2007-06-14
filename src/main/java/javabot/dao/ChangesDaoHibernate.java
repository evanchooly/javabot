package javabot.dao;

import javabot.dao.model.Change;
import javabot.dao.model.Factoid;
import javabot.dao.util.QueryParam;
import org.hibernate.Query;
import org.hibernate.Session;

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
    public Iterator<Change> getChanges(QueryParam qp, Change filter) {

        return (Iterator<Change>) buildFindQuery(qp, filter, false).iterate();

    }

    public void logChange(String message) {

        Change change = new Change();

        change.setMessage(message);
        change.setChangeDate(new Date());

        Session session = getSession();
        org.hibernate.Transaction transaction = session.beginTransaction();
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

    public Long getNumberOfChanges(Change filter) {

        return (Long) buildFindQuery(null, filter, true).uniqueResult();
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


    private Query buildFindQuery(QueryParam qp, Change filter, boolean count) {
        StringBuffer hql = new StringBuffer();

        if (count) {
            hql.append("select count(*) ");
        }
        hql.append(" from Change target where 1=1 ");
        if (filter.getId() != null) {
            hql.append("and target id like :id ");
        }
        if (filter.getMessage() != null) {
            hql.append("and upper(target.message) like :message ");
        }
        if (filter.getChangeDate() != null) {
            hql.append("and upper(target.changeDate) like :date ");
        }

        if (!count && qp != null && qp.hasSort()) {
            hql.append("order by upper(target.").append(qp.getSort()).append(
                    ") ").append((qp.isSortAsc()) ? " asc" : " desc");
        }

        Query query = getSession().createQuery(hql.toString());

        if (filter.getId() != null) {
            query.setParameter("id", "%" + filter.getId() + "%");
        }
        if (filter.getMessage() != null) {
            query.setParameter("message", "%" + filter.getMessage().toUpperCase() + "%");
        }
        if (filter.getChangeDate() != null) {
            query.setParameter("date", "%" + filter.getChangeDate() + "%");
        }

        if (!count && qp != null) {
            query.setFirstResult(qp.getFirst()).setMaxResults(qp.getCount());
        }

        return query;
    }

}