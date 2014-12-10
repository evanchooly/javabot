package javabot.dao;

import com.antwerkz.sofia.Sofia;
import com.mongodb.WriteResult;
import javabot.dao.util.QueryParam;
import javabot.model.Change;
import javabot.model.Karma;
import javabot.model.criteria.ChangeCriteria;
import org.mongodb.morphia.query.Query;

import java.time.LocalDateTime;
import java.util.List;

public class ChangeDao extends BaseDao<Change> {
    public ChangeDao() {
        super(Change.class);
    }

    public void logChange(final String message) {
        final Change change = new Change();
        change.setMessage(message);
        change.setChangeDate(LocalDateTime.now());
        save(change);
    }

    public void logAdd(final String sender, final String key, final String value) {
        logChange(Sofia.factoidAdded(sender, key, value));
    }

    public boolean findLog(final String message) {
        ChangeCriteria criteria = new ChangeCriteria(ds);
        criteria.message().equal(message);
        return criteria.query().countAll() != 0;
    }

    public Long count(final Change filter) {
        return buildFindQuery(null, filter, true).countAll();
    }

    @SuppressWarnings({"unchecked"})
    public List<Change> getChanges(final QueryParam qp, final Change filter) {
        return buildFindQuery(qp, filter, true).asList();
    }

    private Query<Change> buildFindQuery(final QueryParam qp, final Change filter, final boolean count) {
        ChangeCriteria criteria = new ChangeCriteria(ds);
        if (filter.getId() != null) {
            criteria.id().equal(filter.getId());
        }
        if (filter.getMessage() != null) {
            criteria.message().contains(filter.getMessage());
        }
        if (filter.getChangeDate() != null) {
            criteria.changeDate(filter.getChangeDate());
        }
        criteria.changeDate().order(false);
        if (!count && qp != null) {
            criteria.query().offset(qp.getFirst());
            criteria.query().limit(qp.getCount());
        }
        return criteria.query();
    }

    public WriteResult deleteAll() {
        return ds.delete(ds.createQuery(Change.class));
    }
}