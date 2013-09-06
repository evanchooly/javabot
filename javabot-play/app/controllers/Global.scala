package controllers

import com.google.inject.{Stage, Guice, Key}
import com.google.inject.name.Names
import org.pac4j.core.client.Clients
import org.pac4j.play.Config
import play.GlobalSettings
import utils.PlayModule
import org.pac4j.oauth.client.Google2Client
import play.mvc.Action
import play.mvc.Http.{Context, Request}
import java.lang.reflect.Method

class Global extends GlobalSettings {

  private lazy val injector = Guice.createInjector(Stage.PRODUCTION, new PlayModule)

  override def getControllerInstance[A](controllerClass: Class[A]) = {
    injector.getInstance(controllerClass)
  }

  override def onStart(app: play.Application) {
    val key = injector.getInstance(Key.get(classOf[String], Names.named("google.key")))
    val secret = injector.getInstance(Key.get(classOf[String], Names.named("google.secret")))

    Config.setClients(new Clients("http://localhost:9000/oauth2callback", new Google2Client(key, secret)))
    // for test purposes : profile timeout = 60 seconds
    Config.setProfileTimeout(60)
  }

  override def onRequest(p1: Request, p2: Method) = {
    new Action.Simple() {
      def call(ctx: Context): play.mvc.Result = {
        delegate.call(ctx)
      }
    }
  }
}