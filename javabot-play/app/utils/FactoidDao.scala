package utils

import javabot.model.Factoid
import scala.collection.JavaConversions._
import models.FactoidForm
import javabot.model.criteria.FactoidCriteria

class FactoidDao extends javabot.dao.FactoidDao {
  def find(form: FactoidForm, start: Int, count: Int): (Long, Seq[Factoid]) = {
    val criteria = new FactoidCriteria(ds)
    form.name.map( name => criteria.name().contains(name))
    form.value.map( value => criteria.value().contains(value))
    form.user.map( user => criteria.userName().contains(user))

    val query = criteria.query()
    val total = ds.getCount(query)
    query.offset(start)
    query.limit(count)
    Pair(total, query.asList().toSeq)
  }
}
