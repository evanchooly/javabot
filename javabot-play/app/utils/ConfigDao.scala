package utils

import java.util
import javabot.model.{Admin, OperationEvent, EventType}
import models.ConfigInfo

class ConfigDao extends javabot.dao.ConfigDao {

  def save(admin: Admin, info: ConfigInfo) {
    var url = info.url
    while (url.endsWith("/")) {
      url = url.substring(0, url.length - 1)
    }
    val old = get
    old.setServer(info.server)
    old.setHistoryLength(info.historyLength)
    old.setNick(info.nick)
    old.setPassword(info.password)
    old.setPort(info.port)
    old.setTrigger(info.trigger)
    old.setUrl(url)
    save(old)

  }
  def updateOperations(admin: Admin, old: util.List[String], updated: List[String]) {
    Injectables.operations.foreach(operation => {
      if(old.contains(operation) && !updated.contains(operation)) {
        save(new OperationEvent(EventType.DELETE, operation, admin.getUserName))
      } else if (!old.contains(operation) && updated.contains(operation)) {
        save(new OperationEvent(EventType.ADD, operation, admin.getUserName))
      }
    })
  }
}