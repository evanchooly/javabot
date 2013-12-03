package utils

import javabot.model.Change
import javabot.model.criteria.ChangeCriteria
import models.ChangeForm
import scala.collection.JavaConversions._

class ChangeDao extends javabot.dao.ChangeDao {
  def find(form: ChangeForm, start: Int, count: Int): (Long, Seq[Change]) = {
    val criteria = new ChangeCriteria(ds)
    form.message.map( message => criteria.message().contains(message))
    criteria.changeDate().order(false)

    val query = criteria.query()
    val total = ds.getCount(query)
    query.offset(start)
    query.limit(count)
    Pair(total, query.asList().toSeq)
  }
}
