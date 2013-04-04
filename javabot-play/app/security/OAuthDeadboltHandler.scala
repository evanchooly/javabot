package security

import be.objectify.deadbolt.scala.{DynamicResourceHandler, DeadboltHandler}
import play.api.mvc.{Request, Result, Results}
import be.objectify.deadbolt.core.models.Subject
import javabot.model.Admin

class OAuthDeadboltHandler(dynamicResourceHandler: Option[DynamicResourceHandler] = None) extends DeadboltHandler {

  def beforeAuthCheck[A](request: Request[A]) = None

  override def getSubject[A](request: Request[A]): Option[Subject] = {
    Some(request.session.get("user").asInstanceOf[Subject])
  }

  def onAuthFailure[A](request: Request[A]): Result = {
    Results.Unauthorized //Forbidden(views.html.accessFailed())
  }

  def getDynamicResourceHandler[A](request: Request[A]) = None
}