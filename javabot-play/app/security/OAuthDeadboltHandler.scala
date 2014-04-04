package security

import be.objectify.deadbolt.scala.DeadboltHandler
import play.api.mvc.{SimpleResult, Request, Results}
import be.objectify.deadbolt.core.models.Subject

import utils.{Context, AdminDao}
import javax.inject.{Singleton, Inject}
import com.google.inject.Provider
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

@Singleton
class OAuthDeadboltHandler @Inject()(adminDao: AdminDao, contextProvider: Provider[Context]) extends DeadboltHandler {

  def beforeAuthCheck[A](request: Request[A]) = Option.empty[Future[SimpleResult]]

  override def getSubject[A](request: Request[A]): Option[Subject] = {
    for {
      name <- request.session.get("userName")
      subject <- adminDao.getSubject(name)
    } yield subject
  }

  def onAuthFailure[A](request: Request[A]): Future[SimpleResult] = {
    Future {
      Results.Forbidden("who are you anyway?")
    }
  }

  //    Results.Forbidden(views.html.accessFailed(this, contextProvider.get()))

  def getDynamicResourceHandler[A](request: Request[A]) = Option.empty[be.objectify.deadbolt.scala.DynamicResourceHandler]
}