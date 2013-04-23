package controllers

import play.{Application, GlobalSettings}
import utils.Injectables
import com.google.code.morphia.logging.MorphiaLoggerFactory

class Global extends GlobalSettings {

  override def beforeStart(p1: Application) {
    super.beforeStart(p1)
  }

  override def getControllerInstance[A](controllerClass: Class[A]) = Injectables.injector.getInstance(controllerClass)
}
