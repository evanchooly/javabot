import com.google.code.morphia.logging.MorphiaLoggerFactory
import play.{Application, GlobalSettings}
import utils.Injectables

class Global extends GlobalSettings {

  override def beforeStart(p1: Application) {
    println("beforeStart")
    super.beforeStart(p1)
  }

  override def getControllerInstance[A](controllerClass: Class[A]) = {
    println("getting controller")
    Injectables.injector.getInstance(controllerClass)
  }
}
