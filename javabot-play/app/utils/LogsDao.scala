package utils

import java.util.{Collections, Date}
import javabot.model.Logs
import java.util
import org.joda.time.DateTime
import javabot.model.criteria.LogsCriteria
import scala.collection.JavaConversions._

class LogsDao extends javabot.dao.LogsDao {

  private def dailyLog(channelName: String, date: DateTime): List[Logs] = {
    val channel = channelDao.get(channelName)
    var list: List[Logs] = null
    if (channel.getLogged) {
      val start = (if (date == null) new DateTime() else date).withTimeAtStartOfDay
      val tomorrow = start.plusDays(1)
      val criteria = new LogsCriteria(ds)
      criteria.channel().equal(channelName)
      criteria.and(
          criteria.updated().greaterThanOrEq(start.toDate),
          criteria.updated().lessThanOrEq(tomorrow.toDate)
      )
      list = criteria.query().asList().toList
    }
    list
  }

  def findByChannel(name: String, date: DateTime, showAll: Boolean): List[Logs] = {
    val channel = channelDao.get(name)
    var logs: List[Logs] = null
    if (showAll || channel.getLogged) {
      logs = dailyLog(name, date)
    } else {
      logs = List.empty
    }

    logs.foreach(log => print(log.getId + " "))

    logs
  }

}
