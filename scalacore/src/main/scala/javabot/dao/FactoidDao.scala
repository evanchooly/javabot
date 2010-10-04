package javabot.dao

import javabot.model.Factoid
//import com.twitter.querulous.query.SqlQueryFactory

class FactoidDao {
  def find: List[Factoid] = {
//    val queryEvaluator =new SqlQueryFactory
//    val users = queryEvaluator.select("SELECT * FROM users WHERE id IN (?) OR name = ?", List(1, 2, 3), "Jacques") {
//      row => new Factoid(row.getInt("id"), row.getString("name"))
//    }
//    queryEvaluator.execute("INSERT INTO users VALUES (?, ?)", 1, "Jacques")
    //    val query: StringBuilder = new StringBuilder("from Factoid f")
    //    if (qp.hasSort) {
    //      query.append(" order by ").append(qp.getSort).append(if (qp.isSortAsc) " asc" else " desc")
    //    }
    //    return getEntityManager.createQuery(query.toString).setFirstResult(qp.getFirst).setMaxResults(qp.getCount).getResultList
    return List();
  }
}