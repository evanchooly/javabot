import com.google.inject.servlet.RequestScoped
import com.google.inject.{Stage, Guice, Injector}
import play.{Application, GlobalSettings}
import utils.PlayModule

class Global extends GlobalSettings {

  private var injector: Injector = null

  override def onStart(app: Application) {
    super.onStart(app)
    injector = createInjector
  }

  def createInjector: Injector = {
    Guice.createInjector(Stage.PRODUCTION, new PlayModule() {
      override def configurePlay() {

      }
    })
  }

  override def getControllerInstance[A](controllerClass: Class[A]): A = {
    injector.getInstance(controllerClass)
  }
}
