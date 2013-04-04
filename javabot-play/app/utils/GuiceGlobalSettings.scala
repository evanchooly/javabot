package utils

import play.GlobalSettings
import com.google.inject.Injector

abstract class GuiceGlobalSettings extends GlobalSettings {

  var injector: Injector

  override def onStart(application: play.Application) {
    injector = createInjector
  }

  override def getControllerInstance[A](controllerClass: Class[A]) {
    injector.getInstance(controllerClass)
  }

  abstract def createInjector: Injector
}
