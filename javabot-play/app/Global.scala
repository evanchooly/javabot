import com.google.inject.Key
import com.google.inject.name.Names
import org.pac4j.core.client.Clients
import org.pac4j.oauth.client.{Google2Client, TwitterClient, FacebookClient}
import org.pac4j.play.Config
import play.{Application, GlobalSettings}
import utils.Injectables

class Global extends GlobalSettings {

  override def beforeStart(p1: Application) {
    super.beforeStart(p1)
  }

  override def getControllerInstance[A](controllerClass: Class[A]) = {
    Injectables.injector.getInstance(controllerClass)
  }

  override def onStart(app: Application) {
    val key = Injectables.injector.getInstance(Key.get(classOf[String], Names.named("google.key")))
    val secret = Injectables.injector.getInstance(Key.get(classOf[String], Names.named("google.secret")))

    Config.setClients(new Clients("http://localhost:9000/oauth2callback", new Google2Client(key, secret)))
    // for test purposes : profile timeout = 60 seconds
    // Config.setProfileTimeout(60)
  }
}