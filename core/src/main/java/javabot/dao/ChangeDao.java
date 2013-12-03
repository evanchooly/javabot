package javabot.dao;

import java.util.List;

import org.mongodb.morphia.query.Query;
import javabot.model.Change;
import javabot.model.criteria.ChangeCriteria;
import org.joda.time.DateTime;

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