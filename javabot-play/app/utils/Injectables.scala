package utils

import com.google.inject.{Key, Stage, Guice, Injector}
import security.OAuthDeadboltHandler
import javabot.dao.{ApiDao, ChannelDao}
import javabot.model.NickRegistration
import twitter4j.TwitterFactory
import com.google.inject.name.Names

object Injectables {
  def changeDao = injector.getInstance(classOf[ChangeDao])

  def karmaDao = injector.getInstance(classOf[KarmaDao])

  def context = injector.getInstance(classOf[Context])

  def twitter = new TwitterFactory().getInstance

  def apiDao = injector.getInstance(classOf[ApiDao])

  def channelDao = injector.getInstance(classOf[ChannelDao])

  def factoidDao = injector.getInstance(classOf[FactoidDao])

  def handler: OAuthDeadboltHandler = injector.getInstance(classOf[OAuthDeadboltHandler])

  def adminDao = injector.getInstance(classOf[AdminDao])

  def configDao =  injector.getInstance(classOf[ConfigDao])

  def logsDao =  injector.getInstance(classOf[LogsDao])

  def operations = injector.getInstance(Key.get(classOf[java.util.List[String]], Names.named("operations")))

  def config = configDao.findAll.get(0)

  def admins = adminDao.findAll

  def injector: Injector = {
    Guice.createInjector(Stage.PRODUCTION, new PlayModule)
  }
}
