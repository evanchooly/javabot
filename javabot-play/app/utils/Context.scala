package utils

import javabot.dao.ChannelDao
import javabot.model.Logs
import javax.inject.Inject
import java.time.LocalDateTime
import play.api.mvc
import play.api.mvc.AnyContent

class Context @Inject()(factoidDao: FactoidDao, channelDao: ChannelDao, adminDao: AdminDao, logsDao: LogsDao) {

  var logs: List[Logs] = null

  private var request: mvc.Request[AnyContent] = null

  def request(request: mvc.Request[AnyContent]) {
    this.request = request
  }

  var today = new DateTime

  var channel: String = null

  def factoidCount = factoidDao.count()

  def showAll: Boolean = {
    val userName = request.session.get("userName")

    userName.exists(name => {
      val admin = adminDao.getAdmin(name)
      admin != null && admin.getBotOwner
    })
  }

  def channels = {
    channelDao.findLogged(showAll)
  }

  def getLogs = {
    logs
  }

  def logChannel(name: String, date: DateTime) {
    channel = name
    logs = logsDao.findByChannel(channel, date, showAll)
  }
}
