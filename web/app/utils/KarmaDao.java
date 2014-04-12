package utils;

import java.util.List;

import javabot.model.Karma;
import javabot.model.criteria.KarmaCriteria;
import play.libs.F.Tuple;

public class KarmaDao extends javabot.dao.KarmaDao {
  public Tuple<Long, List<Karma>> find(int start, int count) {
    KarmaCriteria criteria = new KarmaCriteria(ds);
    criteria.value().order(false);
    long total = criteria.query().countAll();
    criteria.query().offset(start);
    criteria.query().limit(count);
    return new Tuple<>(total, criteria.query().asList());
  }
}
