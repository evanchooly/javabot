package security

import be.objectify.deadbolt.scala.{DynamicResourceHandler, DeadboltHandler}
import play.api.mvc.{Request, Result, Results}
import be.objectify.deadbolt.core.models.Subject
import com.google.inject.Inject

import models.Admin
import utils.AdminDao

class OAuthDeadboltHandler(dynamicResourceHandler: Option[DynamicResourceHandler] = None)
  extends DeadboltHandler {

  @Inject
  private var adminDao: AdminDao = null

  def beforeAuthCheck[A](request: Request[A]) = Option.empty[Result]

  override def getSubject[A](request: Request[A]): Option[Subject] = {
    val option: Option[String] = request.session.get("user")
    if(option.isEmpty) Option.empty[Subject] else Option[Subject](adminDao.getAdmin(option.get))
  }

  def onAuthFailure[A](request: Request[A]): Result = {
    Results.Unauthorized //Forbidden(views.html.accessFailed())
  }

  def getDynamicResourceHandler[A](request: Request[A]) = Option.empty[be.objectify.deadbolt.scala.DynamicResourceHandler]
}