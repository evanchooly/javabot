package utils

import com.google.inject.{Stage, Guice, Injector}
import java.io.File
import javabot.dao.{ApiDao, ChannelDao}
import play.Play
import play.api.libs.Files
import play.api.mvc.{AnyContent, Request}
import security.OAuthDeadboltHandler
import twitter4j.TwitterFactory

object Injectables {
  def changeDao = injector.getInstance(classOf[ChangeDao])

  def karmaDao = injector.getInstance(classOf[KarmaDao])

  def context(request: Request[AnyContent]): Context = {
    val context: Context = injector.getInstance(classOf[Context])
    context.request(request)
    context
  }

  def twitter = new TwitterFactory().getInstance

  def apiDao = injector.getInstance(classOf[ApiDao])

  def channelDao = injector.getInstance(classOf[ChannelDao])

  def factoidDao = injector.getInstance(classOf[FactoidDao])

  def handler: OAuthDeadboltHandler = injector.getInstance(classOf[OAuthDeadboltHandler])

  def adminDao = injector.getInstance(classOf[AdminDao])

  def configDao = injector.getInstance(classOf[ConfigDao])

  def logsDao = injector.getInstance(classOf[LogsDao])

  def operations: List[String] = {
    val file: File = Play.application.getFile("conf/operations.list")
    (if (file.exists) Files.readFile(file).split('\n') else new Array[String](0)).toList
  }

  def config = configDao.get

  def admins = adminDao.findAll

  def injector: Injector = {
    Guice.createInjector(Stage.PRODUCTION, new PlayModule)
  }
}