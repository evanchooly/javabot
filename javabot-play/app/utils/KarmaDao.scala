package utils

import javabot.model.Karma
import javabot.model.criteria.KarmaCriteria
import scala.collection.JavaConversions._

class KarmaDao extends javabot.dao.KarmaDao {
  def find(start: Int, count: Int): (Long, Seq[Karma]) = {
    val criteria = new KarmaCriteria(ds)
    criteria.value().order(false)
    val total = criteria.query.countAll

    criteria.query.offset(start)
    criteria.query.limit(count)
    Pair(total, criteria.query.asList().toSeq)
  }
}
