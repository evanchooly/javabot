package utils

import controllers.AdminController
import javabot.model.{Admin, OperationEvent, EventType}
import java.util
import javax.inject.{Named, Inject}

class ConfigDao extends javabot.dao.ConfigDao {

  def updateOperations(admin: Admin, old: util.List[String], updated: util.List[String]) {
    Injectables.operations.foreach(operation => {
      var eventType: EventType = null
      if(old.contains(operation) && !updated.contains(operation)) {
        eventType = EventType.DELETE
      } else if (!old.contains(operation) && updated.contains(operation)) {
        eventType = EventType.ADD
      }
      if(eventType != null) {
        save(new OperationEvent(eventType, operation, admin.getUserName))
      }
    })
  }
}