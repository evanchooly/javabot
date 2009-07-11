package javabot.dao.impl;

import java.util.Date;
import java.util.List;
import javax.persistence.Query;

import javabot.dao.AbstractDaoImpl;
import javabot.dao.ChangeDao;
import javabot.dao.util.QueryParam;
import javabot.model.Change;

public class ChangeDaoImpl extends AbstractDaoImpl<Change> implements ChangeDao {
    public ChangeDaoImpl() {
        super(Change.class);
    }

    @SuppressWarnings("unchecked")
    public List<Change> getChanges(final QueryParam qp, final Change filter) {
        return buildFindQuery(qp, filter, false).getResultList();
    }

    public void logChange(final String message) {
        final Change change = new Change();
        change.setMessage(message);
        change.setChangeDate(new Date());
        save(change);
    }

    public void logAdd(final String sender, final String key, final String value) {
        logChange(sender + " added '" + key + "' with a value of '" + value + "'");
    }

    public boolean findLog(final String message) {
        final String query = "select c from Change c where c.message = :message";
        final List change = getEntityManager().createQuery(query)
                .setParameter("message", message)
                .getResultList();
        return change != null && !change.isEmpty();
    }

    public Long count(final Change filter) {
        return (Long) buildFindQuery(null, filter, true).getSingleResult();
    }

    @SuppressWarnings({"unchecked"})
    public List<Change> get(final Change filter) {
        return (List<Change>)buildFindQuery(null, filter, false).getResultList();
    }

    // TODO clean this up
    private Query buildFindQuery(final QueryParam qp, final Change filter, final boolean count) {
        final StringBuilder hql = new StringBuilder();
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
            hql.append("and target.changeDate like :date ");
        }
        if (!count && qp != null && qp.hasSort()) {
            hql.append("order by upper(target.").append(qp.getSort()).append(
                ") ").append(qp.isSortAsc() ? " asc" : " desc");
        }

        final Query query = getEntityManager().createQuery(hql.toString());
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