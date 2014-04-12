package utils;

import java.util.List;

import javabot.model.Change;
import javabot.model.criteria.ChangeCriteria;
import org.mongodb.morphia.query.Query;
import play.libs.F.Tuple;

public class ChangeDao extends javabot.dao.ChangeDao {
  public Tuple<Long, List<Change>> find(Change change, int start, int count) {
    ChangeCriteria criteria = new ChangeCriteria(ds);
    if(change.getMessage() != null) {
      criteria.message().contains(change.getMessage());
    }
    criteria.changeDate().order(false);

    Query<Change> query = criteria.query();
    long total = ds.getCount(query);
    query.offset(start);
    query.limit(count);
    return new Tuple<>(total, query.asList());
  }

}
