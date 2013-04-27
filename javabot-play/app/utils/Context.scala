package utils

import javax.inject.Inject
import javabot.dao.{ChannelDao, FactoidDao}
import javabot.model.Logs
import java.util.Date
import java.util
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

  var channel: String = "#jbunittest"

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

  def logChannel(channel: String,  date: DateTime) {
    logs = logsDao.findByChannel(channel, date, showAll)
  }
}
