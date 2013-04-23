package utils

import be.objectify.deadbolt.core.models.{Permission, Role, Subject}
import java.util.Collections
import java.util
import javabot.model.Admin

class AdminDao extends javabot.dao.AdminDao {
  def getSubject(userName: String): Option[Subject] = {
    println("************ " + userName)
    getAdmin(userName)
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
