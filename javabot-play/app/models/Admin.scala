package models

import be.objectify.deadbolt.core.models.Subject
import java.util.Collections

class Admin extends javabot.model.Admin with Subject {
  def getIdentifier = getIrcName

  def getPermissions = Collections.emptyList

  def getRoles = Collections.emptyList
}
