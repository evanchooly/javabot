package security

import be.objectify.deadbolt.scala.{DynamicResourceHandler, DeadboltHandler}
import play.api.mvc.{SimpleResult, Request, Result, Results}
import be.objectify.deadbolt.core.models.Subject

import models.Admin
import utils.{Context, AdminDao}
import java.io.Serializable
import scala.None
import javax.inject.{Singleton, Inject}
import com.google.inject.Provider
import scala.concurrent.Future

@Singleton
class OAuthDeadboltHandler @Inject()(adminDao: AdminDao, contextProvider: Provider[Context]) extends DeadboltHandler {

  def beforeAuthCheck[A](request: Request[A]) = Option.empty[Future[SimpleResult]]

  override def getSubject[A](request: Request[A]): Option[Subject] = {
    for {
      name <- request.session.get("userName")
      subject <- adminDao.getSubject(name)
    } yield subject
  }

  def onAuthFailure[A](request: Request[A]): Result = {
    Results.Forbidden("who are you anyway?")
//    Results.Forbidden(views.html.accessFailed(this, contextProvider.get()))
  }

  def getDynamicResourceHandler[A](request: Request[A]) = Option.empty[be.objectify.deadbolt.scala.DynamicResourceHandler]
}