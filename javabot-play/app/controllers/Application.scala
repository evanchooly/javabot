package controllers

import play.api.mvc._
import models.Channel
import java.util.Date
import play.mvc.With
import utils.RequestScopedAction
import security.OAuthDeadboltHandler

object Application extends Controller {

  @With(classOf[RequestScopedAction])
  def index = Action {
    Ok(views.html.index(new OAuthDeadboltHandler, "welcome", List.empty[Channel]))
  }

  def factoids = TODO

  def changes = TODO

  def karma = TODO

  def logs(name: String, dateString: String) = TODO
}