package controllers

import play.api.mvc._
import models.Channel
import java.util.Date
import play.mvc.With
import utils.{AdminDao, RequestScopedAction}
import security.OAuthDeadboltHandler
import com.google.inject.{Injector, Inject}

object Application extends Controller {
  @Inject
  private var injector: Injector = null

  @With(Array(classOf[RequestScopedAction]))
  def index = Action {
    implicit request =>
      val handler: OAuthDeadboltHandler = injector.getInstance(classOf[OAuthDeadboltHandler])
      Ok(views.html.index(new OAuthDeadboltHandler, "welcome", List.empty[Channel]))
  }

  def factoids = TODO

  def changes = TODO

  def karma = TODO

  def logs(name: String, dateString: String) = TODO
}