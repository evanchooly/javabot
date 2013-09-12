package utils

import java.util.{Collections, Date}
import javabot.model.Logs
import java.util
import org.joda.time.{DateTimeZone, DateTime}
import javabot.model.criteria.LogsCriteria
import scala.collection.JavaConversions._
import java.net.URLDecoder

class LogsDao extends javabot.dao.LogsDao {

  private def dailyLog(channelName: String, date: DateTime, logged: Boolean): List[Logs] = {
    val channel = channelDao.get(channelName)
    var list: List[Logs] = null
    if (logged) {
      val start = (if (date == null) new DateTime(DateTimeZone.forID("US/Eastern")) else date).withTimeAtStartOfDay

      val tomorrow = start.plusDays(1)
      val criteria = new LogsCriteria(ds)
      criteria.channel(channelName)
      criteria.and(
          criteria.updated().lessThanOrEq(tomorrow),
          criteria.updated().greaterThanOrEq(start)
      )

      list = criteria.query().asList().toList
    }
    list
  }

  def findByChannel(name: String, date: DateTime, showAll: Boolean): List[Logs] = {
    val channel = channelDao.get(name)
    var logs: List[Logs] = null
    if (channel != null && (showAll || channel.getLogged)) {
      dailyLog(name, date, showAll || channel.getLogged)
    } else {
      List.empty
    }
  }

}
