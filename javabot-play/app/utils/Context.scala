package utils

import java.util.Date
import javabot.dao.ChannelDao
import javabot.model.Logs
import javax.inject.Inject
import org.joda.time.DateTime

class Context {

  @Inject
  private var factoidDao: FactoidDao = null
  @Inject
  private var channelDao:ChannelDao = null
  @Inject
  private var adminDao:AdminDao = null
  @Inject
  private var logsDao:LogsDao = null

  var logs : List[Logs] = null

  @Inject
  private var twitterContext: TwitterContext = null

  var today = new Date

  var channel: String = null

  def factoidCount = factoidDao.count()

  def showAll: Boolean = {
    var showAll = false
    twitterContext = controllers.AdminController.getTwitterContext
    if(twitterContext != null) {
      if(twitterContext.screenName != null) {
        val admin = adminDao.getAdmin(twitterContext.screenName)
        showAll = admin != null && admin.getBotOwner
      }
    }
    showAll
  }

  def channels = {
    channelDao.findLogged(showAll)
  }

  def getLogs = {
    logs
  }

  def logChannel(name: String,  date: DateTime) {
    channel = name
    logs = logsDao.findByChannel(name, date, showAll)
  }
}
