package controllers

import play.GlobalSettings
import utils.Injectables

class Global extends GlobalSettings {
  override def getControllerInstance[A](controllerClass: Class[A]) = Injectables.injector.getInstance(controllerClass)
}
