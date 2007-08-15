package javabot.dao.impl;

import java.util.Date;
import java.util.List;
import javax.persistence.Query;

import javabot.dao.AbstractDaoHibernate;
import javabot.dao.ChangeDao;
import javabot.dao.util.QueryParam;
import javabot.model.Change;

// User: joed
// Date: Apr 11, 2007
// Time: 2:41:22 PM

public class ChangeDaoHibernate extends AbstractDaoHibernate<Change> implements ChangeDao {
    public ChangeDaoHibernate() {
        super(Change.class);
    }

    @SuppressWarnings("unchecked")
    public List<Change> getChanges(QueryParam qp, Change filter) {
        return buildFindQuery(qp, filter, false).getResultList();
    }

    public void logChange(String message) {
        Change change = new Change();
        change.setMessage(message);
        change.setChangeDate(new Date());
        save(change);
    }

    public void logAdd(String sender, String key, String value) {
        logChange(sender + " added '" + key + "' with a value of '" + value + "'");
    }

    public boolean findLog(String message) {
        String query = "from Change c where c.message = :message";
        List change = getEntityManager().createQuery(query)
            .setParameter("message", message)
            .getResultList();
        return change != null && ! change.isEmpty();
    }

    public Long count(Change filter) {
        return (Long)buildFindQuery(null, filter, true).getSingleResult();
    }

    // TODO clean this up
    private Query buildFindQuery(QueryParam qp, Change filter, boolean count) {
        StringBuilder hql = new StringBuilder();
        if(count) {
            hql.append("select count(*) ");
        }
        hql.append(" from Change target where 1=1 ");
        if(filter.getId() != null) {
            hql.append("and target id like :id ");
        }
        if(filter.getMessage() != null) {
            hql.append("and upper(target.message) like :message ");
        }
        if(filter.getChangeDate() != null) {
            hql.append("and upper(target.changeDate) like :date ");
        }
        if(!count && qp != null && qp.hasSort()) {
            hql.append("order by upper(target.").append(qp.getSort()).append(
                ") ").append((qp.isSortAsc()) ? " asc" : " desc");
        }

        Query query = getEntityManager().createQuery(hql.toString());
        if(filter.getId() != null) {
            query.setParameter("id", "%" + filter.getId() + "%");
        }
        if(filter.getMessage() != null) {
            query.setParameter("message", "%" + filter.getMessage().toUpperCase() + "%");
        }
        if(filter.getChangeDate() != null) {
            query.setParameter("date", "%" + filter.getChangeDate() + "%");
        }
        if(!count && qp != null) {
            query.setFirstResult(qp.getFirst()).setMaxResults(qp.getCount());
        }
        return query;
    }

}