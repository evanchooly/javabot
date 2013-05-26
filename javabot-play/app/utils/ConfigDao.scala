package utils

import controllers.AdminController
import javabot.model.{OperationEvent, EventType}
import java.util
import javax.inject.{Named, Inject}

class ConfigDao extends javabot.dao.ConfigDao {
  @Inject
  @Named("operations")
  var OPERATIONS: List[String] = null

  def updateOperations(old: util.Set[String], updated: util.Set[String]) {
    OPERATIONS.foreach(operation => {
      var eventType: EventType = null
      if(old.contains(operation) && !updated.contains(operation)) {
        eventType = EventType.DELETE
      } else if (!old.contains(operation) && updated.contains(operation)) {
        eventType = EventType.ADD
      }
      if(eventType != null) {
        save(new OperationEvent(eventType, operation, AdminController.getTwitterContext.screenName))
      }
    })
  }

}
