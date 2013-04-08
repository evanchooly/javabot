package models

import be.objectify.deadbolt.core.models.{Permission, Subject}
import java.util.Collections
import java.util

class Admin extends javabot.model.Admin with Subject {
  def getIdentifier = getIrcName

  def getPermissions = new util.ArrayList

  def getRoles = new util.ArrayList
}
