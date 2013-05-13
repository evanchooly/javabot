package javabot.dao;

import java.util.List;

import com.google.code.morphia.query.Query;
import javabot.dao.util.QueryParam;
import javabot.model.Change;
import javabot.model.criteria.ChangeCriteria;
import org.joda.time.DateTime;

public class ChangeDao extends BaseDao<Change> {
    public ChangeDao() {
        super(Change.class);
    }

    @SuppressWarnings("unchecked")
    public List<Change> getChanges(final QueryParam qp, final Change filter) {
        return buildFindQuery(qp, filter).asList();
    }

    public void logChange(final String message) {
        final Change change = new Change();
        change.setMessage(message);
        change.setChangeDate(new DateTime());
        save(change);
    }

    public void logAdd(final String sender, final String key, final String value) {
        logChange(sender + " added '" + key + "' with a value of '" + value + "'");
    }

    public boolean findLog(final String message) {
        ChangeCriteria criteria = new ChangeCriteria(ds);
        criteria.message().equal(message);
        return criteria.query().countAll() != 0;
    }

    public Long count(final Change filter) {
        return buildFindQuery(null, filter).countAll();
    }

    @SuppressWarnings({"unchecked"})
    public List<Change> get(final Change filter) {
        return (List<Change>) buildFindQuery(null, filter).asList();
    }

    private Query buildFindQuery(final QueryParam qp, final Change filter) {
        ChangeCriteria criteria = new ChangeCriteria(ds);
        if (filter.getId() != null) {
            criteria.id().equal(filter.getId());
        }
        if (filter.getMessage() != null) {
            criteria.query().filter("upper(message) like ", filter.getMessage().toUpperCase());
        }
        if (filter.getChangeDate() != null) {
            criteria.changeDate().equal(filter.getChangeDate());
        }
        if (qp != null && qp.hasSort()) {
            String sortOrder = qp.isSortAsc() ? "" : "-";
            criteria.query().order(sortOrder + qp.getSort());
        } else if (qp == null || !qp.hasSort()) {
            criteria.query().order("-changeDate");
        }
        return criteria.query();
    }
}
