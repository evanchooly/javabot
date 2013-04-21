package security

import be.objectify.deadbolt.scala.{DynamicResourceHandler, DeadboltHandler}
import play.api.mvc.{Request, Result, Results}
import be.objectify.deadbolt.core.models.Subject
import com.google.inject.Inject

import models.Admin
import utils.AdminDao

class OAuthDeadboltHandler(@Inject adminDao: AdminDao) extends DeadboltHandler {

  def beforeAuthCheck[A](request: Request[A]) = Option.empty[Result]

  override def getSubject[A](request: Request[A]): Option[Subject] = {
    adminDao.getSubject(request.session.get("user").get)
  }

  def onAuthFailure[A](request: Request[A]): Result = {
    Results.Unauthorized //Forbidden(views.html.accessFailed())
  }

  def getDynamicResourceHandler[A](request: Request[A]) = Option.empty[be.objectify.deadbolt.scala.DynamicResourceHandler]
}