package utils;

import java.util.List;

import com.antwerkz.critter.TypeSafeFieldEnd;
import javabot.model.Factoid;
import javabot.model.criteria.FactoidCriteria;
import org.mongodb.morphia.query.Query;
import play.libs.F.Tuple;

public class FactoidDao extends javabot.dao.FactoidDao {
  public Tuple<Long, List<Factoid>> find(Factoid factoid, int start, int count) {
    FactoidCriteria criteria = new FactoidCriteria(ds);
    add(factoid.getName(), criteria.upperName());
    add(factoid.getValue(), criteria.value());
    add(factoid.getUserName(), criteria.userName());

    criteria.name().order();

    Query<Factoid> query = criteria.query();
    long total = ds.getCount(query);
    query.offset(start);
    query.limit(count);
    return new Tuple<Long, List<Factoid>>(total, query.asList());
  }

  private void add(final String value, final TypeSafeFieldEnd<FactoidCriteria, Factoid, String> fieldEnd) {
    if(value != null) {
      fieldEnd.containsIgnoreCase(value);
    }
  }
}
