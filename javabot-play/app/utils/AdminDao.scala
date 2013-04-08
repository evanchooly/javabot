package utils

import models.Admin
import javabot.dao.BaseDao
import javabot.model.criteria.AdminCriteria
import java.util.Date

class AdminDao extends BaseDao[Admin](classOf[Admin]) {

  def isAdmin(user: String, hostName: String): Boolean = {
    findAll().isEmpty || getAdmin(user, hostName) != null
  }

  def getAdmin(ircName: String, hostName: String): Admin = {
    ds.createQuery(classOf[Admin])
      .filter("ircName =", ircName)
      .filter("hostName = ", hostName)
      .get()
  }

  def getAdmin(userName: String): Admin = {
    ds.createQuery(classOf[Admin])
      .filter("userName =", userName)
      .get()
  }

  def create(ircName: String, userName: String, hostName: String) {
    val admin = new Admin()
    admin.setIrcName(ircName)
    admin.setUserName(userName)
    admin.setHostName(hostName)
    admin.setUpdated(new Date())
    save(admin)
  }
}