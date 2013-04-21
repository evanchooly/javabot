package controllers

import com.google.inject.{Injector, Inject}
import utils.{Context, Injectables}
import play.api.mvc.{Action, Controller}

object Application extends Controller {
  def index = Action {
    implicit request =>
      Ok(views.html.index(Injectables.handler, new Context))
  }

  def factoids = TODO

  def changes = TODO

  def karma = TODO

  def logs(name: String, dateString: String) = TODO
}