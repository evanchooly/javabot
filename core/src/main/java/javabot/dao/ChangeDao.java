package javabot.dao;

import com.antwerkz.sofia.Sofia;
import javabot.model.Change;
import javabot.model.criteria.ChangeCriteria;
import org.mongodb.morphia.query.Query;

import java.time.LocalDateTime;
import java.util.List;

public class ChangeDao extends BaseDao<Change> {
    public ChangeDao() {
        super(Change.class);
    }

    @SuppressWarnings("unchecked")
    public List<Change> getChanges(final Change filter) {
        return buildFindQuery(filter).asList();
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
        Query<Change> query = criteria.query();
        List<Change> changes = query.asList();
        return query.countAll() != 0;
    }

    public Long count(final Change filter) {
        return buildFindQuery(filter).countAll();
    }

    @SuppressWarnings({"unchecked"})
    public List<Change> get(final Change filter) {
        return buildFindQuery(filter).asList();
    }

    private Query<Change> buildFindQuery(final Change filter) {
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
        criteria.changeDate().order(false);
        return criteria.query();
    }
}