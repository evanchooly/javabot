package utils

import javabot.model.Karma
import javabot.model.criteria.KarmaCriteria
import scala.collection.JavaConversions._

class KarmaDao extends javabot.dao.KarmaDao {
  def find(start: Int, count: Int): (Long, Seq[Karma]) = {
    val query = ds.createQuery(classOf[Karma])
    val total = ds.getCount(query)

    query.offset(start)
    query.limit(count)
    Pair(total, query.asList().toSeq)
  }
}
