package controllers

import java.io.File
import java.util
import javabot.dao.{ChannelDao, FactoidDao, AdminDao, ApiDao}
import javabot.model.{Channel, Factoid, ApiEvent, Config}
import models.{ChannelInfo, Admin}
import org.bson.types.ObjectId
import play.api.data.Forms._
import play.api.data._
import play.api.data.format.{Formats, Formatter}
import play.api.libs.Files
import play.api.mvc._
import twitter4j.TwitterException
import twitter4j.auth.RequestToken
import utils.Implicits._
import utils.{Injectables, TwitterContext}
import play.api.templates.Html
import com.google.inject.Inject

object AdminController extends Controller {
  @Inject
  var channelDao: ChannelDao = null

  val channelForm: Form[ChannelInfo] = Form(
    mapping(
      "id" -> of[ObjectId],
      "name" -> nonEmptyText,
      "key" -> text,
      "logged" -> boolean
    )(ChannelInfo.apply)(ChannelInfo.unapply)
  )

  def oauth = Action {
    implicit request =>
      val context = Injectables.context
      try {
        //        if (twitterContext == null || twitterContext.screenName == null) {
        val twitter = Injectables.twitter
        val requestToken: RequestToken = twitter.getOAuthRequestToken(request.uri + "/callback")
        Redirect(requestToken.getAuthenticationURL)
        //        }
      }
      catch {
        case _: Exception => {
          Unauthorized(views.html.index(Injectables.handler, context))
        }
      }
      Unauthorized(views.html.index(Injectables.handler, context))
  }

  def login = {
    Application.index
  }

  def callback(oauth_token: String, oauth_verifier: String) {
    try {
      val twitter = Injectables.twitter
      val token = twitter.getOAuthAccessToken(new RequestToken(oauth_token, oauth_verifier))
      val screenName = token.getScreenName

      val dao: AdminDao = Injectables.adminDao
      if (dao.findAll.isEmpty) {
        val admin = new Admin("", "", getTwitterContext.screenName, "auto added bot owner")
        admin.setBotOwner(true)
        dao.save(admin)
      }
      Redirect("/").withSession(
        "accessToken" -> token.getToken
      )

    }
    catch {
      case e: TwitterException => {
        System.out.println("e = " + e)
        throw new RuntimeException(e.getMessage, e)
      }
    }
  }

  //  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  def index = Action {
    implicit request =>
      Ok(views.html.admin.config(Injectables.handler, Injectables.context, Injectables.config, Injectables.operations))
  }

  //  @Get("/config")
  //  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  def config = Action {
    implicit request =>
      val context = Injectables.context
      val config = Injectables.configDao.findAll.get(0)
      Ok(views.html.admin.config(Injectables.handler, context, config, Injectables.operations))
  }

  //  @Get("/saveConfig")
  //  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  def saveConfig(config: Config) {
    val configDao = Injectables.configDao
    val all = configDao.findAll
    val old = all.get(0)
    if (!(old.getOperations.equals(config.getOperations))) {
      val operations: util.Set[String] = old.getOperations
      configDao.updateOperations(operations, config.getOperations)
    }
    config.setId(old.getId)
    val updated = config
    val url = updated.getUrl
    while (url.endsWith("/")) {
      config.setUrl(url.substring(0, url.length - 1))
    }
    configDao.save(updated)
    index
  }

  //  @Get("/javadoc")
  //  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  def javadoc = Action {
    implicit request =>
      Ok(views.html.admin.javadoc(Injectables.handler, Injectables.context, Injectables.apiDao.findAll))
  }

  //  @Post("/addJavadoc")
  //  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  def addJavadoc/*(name: String, packages: String, baseUrl: String, file: File)*/ = Action {
    implicit request =>
/*
    val savedFile: File = File.createTempFile("javadoc", ".jar")
    Files.copyFile(file, savedFile)
    val apiDao: ApiDao = Injectables.apiDao
    apiDao.save(new ApiEvent(apiDao.find(name) == null, AdminController.getTwitterContext.screenName, name,
      packages, baseUrl, savedFile))
*/
    Ok(views.html.admin.javadoc(Injectables.handler, Injectables.context, Injectables.apiDao.findAll))
  }

  //  @Get("/deleteApi")
  //  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  def deleteApi(id: ObjectId) = {
    val adminDao = Injectables.adminDao
    val apiDao = Injectables.apiDao
    val event = new ApiEvent(apiDao.find(id).getName, AdminController.getTwitterContext.screenName)
    apiDao.save(event)
    javadoc
  }

  //  @Post("/addAdmin")
  //  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  def addAdmin(ircName: String, hostName: String, twitter: String) {
    if (!twitter.isEmpty) {
      val dao: AdminDao = Injectables.adminDao
      dao.save(new Admin(ircName, hostName, twitter, getTwitterContext.screenName))
    }
    index
  }

  //  @Get("/deleteAdmin")
  //  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  def deleteAdmin(id: ObjectId) {
    val dao: AdminDao = Injectables.adminDao
    val admin = dao.find(id)
    if (admin != null && !admin.getBotOwner) {
      dao.delete(admin)
    }
    index
  }

  //  @Post("/updateAdmin")
  //  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  def updateAdmin(userName: String, ircName: String) {
    val dao = Injectables.adminDao
    val admin = dao.getAdmin(userName)
    admin.setIrcName(ircName)
    dao.save(admin)
    index
  }

  //  @Get("/addChannel")
  //  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  def addChannel() = Action {
    implicit request =>
      Ok(views.html.admin.editChannel(Injectables.handler, Injectables.context, channelForm.fill(new Channel)))
  }

  //  @Get("/showChannel")
  //  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  def showChannel(name: String = null) = Action {
    implicit request =>
      val channel: Channel = Injectables.channelDao.get(name)
      Ok(views.html.admin.editChannel(Injectables.handler, Injectables.context, channelForm.fill(channel)))
  }

  //  @Get("/editChannel")
  def editChannel(channel: Channel) = Action {
    implicit request =>

      Ok(views.html.admin.editChannel(Injectables.handler, Injectables.context, channelForm.fill(channel)))
  }

  //  @Get("/saveChannel")
  //  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  def saveChannel = Action {
    implicit request =>
      channelForm.bindFromRequest.fold(
        errors => BadRequest(views.html.admin.editChannel(Injectables.handler, Injectables.context, errors)),
        channelInfo => {
          save(channelInfo)
          Ok(views.html.admin.config(Injectables.handler, Injectables.context, Injectables.config, Injectables.operations))
        })
  }


  def save(channelInfo: ChannelInfo) {
    var dao: ChannelDao = Injectables.channelDao
    val channel = if (channelInfo.id == null) {
      dao.find(channelInfo.id)
    } else new Channel
    channel.setName(channelInfo.name)
    channel.setLogged(channelInfo.logged)
    channel.setKey(channelInfo.key)
    dao.save(channel)
  }

  //  @Get("/toggleLock")
  //  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  def toggleLock(id: ObjectId) = Action {
    implicit request =>
      val dao: FactoidDao = Injectables.factoidDao
      val factoid: Factoid = dao.find(id)
      factoid.setLocked(!factoid.getLocked)
      dao.save(factoid)
      request.body.asText.map {
        text =>
          Ok(factoid.getLocked.toString)
      }.getOrElse {
        BadRequest("Expecting text/plain request body")
      }

  }

  def getTwitterContext: TwitterContext = {
    //    Cache.get(session.getId + CONTEXT_NAME).asInstanceOf[TwitterContext]
    null
  }


  private final val CONTEXT_NAME: String = "-context"

  //  @Inject
  //  @Named("twitterKey")
  //  var twitterKey: String
  //
  //  @Inject
  //  @Named("twitterSecret")
  //  var twitterSecret: String

}