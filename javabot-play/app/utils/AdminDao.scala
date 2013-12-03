package utils

import be.objectify.deadbolt.core.models.{Permission, Role, Subject}
import java.util
import javabot.model.Admin

class AdminDao extends javabot.dao.AdminDao {
  def getSubject(userName: String): Option[Subject] = {
    val admin = getAdmin(userName)
    Option(if (admin != null) new JavabotSubject(admin) else null)
  }

  class JavabotSubject(admin: Admin) extends Subject {
    def getPermissions = {
      new util.ArrayList[Permission]
    }

    def getIdentifier = {
      admin.getUserName
    }

    def getRoles = {
      util.Arrays.asList(new Role() {
        def getName = "botAdmin"
      })
    }
  }
}
