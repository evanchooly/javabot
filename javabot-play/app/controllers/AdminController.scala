package controllers

import be.objectify.deadbolt.scala.DeadboltActions
import com.google.inject.Inject
import javabot.dao.ChannelDao
import javabot.model.{Config, Factoid, Channel, ApiEvent, Admin}
import models.{AdminForm, ChannelInfo}
import org.bson.types.ObjectId
import org.pac4j.play.scala.ScalaController
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import scala.collection.JavaConversions._
import utils.Implicits._
import utils.{FactoidDao, AdminDao, Injectables}

object AdminController extends ScalaController with DeadboltActions {
  @Inject
  var channelDao: ChannelDao = null

  val adminForm: Form[AdminForm] = Form(
    mapping(
      "ircName" -> optional(text),
      "hostName" -> optional(text),
      "email" -> text
    )(AdminForm.apply)(AdminForm.unapply))

  val channelForm: Form[ChannelInfo] = Form(
    mapping(
      "id" -> of[ObjectId],
      "name" -> nonEmptyText,
      "key" -> text,
      "logged" -> boolean
    )(ChannelInfo.apply)(ChannelInfo.unapply))

  val configForm: Form[Config] = Form(
    mapping(
      "id" -> of[ObjectId],
      "server" -> text,
      "url" -> text,
      "port" -> number,
      "historyLength" -> number,
      "trigger" -> text,
      "nick" -> text,
      "password" -> text,
      "operations" -> list(text)
    )(fromForm)
      (toForm))


  def toForm: (Config) => Some[(ObjectId, String, String, Int, Int, String, String, String, List[String])] = {
    (config: Config) =>
      Some(config.getId, config.getServer, config.getUrl, config.getPort,
      config.getHistoryLength, config.getTrigger, config.getNick, config.getPassword,
      config.getOperations.toList)
  }

  def fromForm: (ObjectId, String, String, Int, Int, String, String, String, List[String]) => Config = {
    (id, server, url, port, historyLength, trigger, nick, password, operations) =>
      new Config(id, server, url, port, historyLength, trigger, nick, password, operations)
  }

  def login = RequiresAuthentication("Google2Client") {
    profile =>
      Action {
        implicit request =>
          val dao: utils.AdminDao = Injectables.adminDao
          if (dao.findAll().isEmpty) {
            dao.create("", profile.getEmail, "")
          }
          Application.index(request).withSession(
            "userName" -> profile.getEmail
          )
      }
  }

  //  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  def index = RequiresAuthentication("Google2Client") {
    profile =>
      Action {
        implicit request =>
          val config: Config = Injectables.config
          Ok(views.html.admin.config(Injectables.handler, Injectables.context(request), config,
            Injectables.operations))
      }
  }

  def config = Restrict(Array("botAdmin"), Injectables.handler) {
    Action {
      implicit request =>
        Ok(views.html.admin.config(Injectables.handler, Injectables.context(request),
          Injectables.config, Injectables.operations))
    }
  }

  //  @Get("/saveConfig")
  //  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  def saveConfig = RequiresAuthentication("Google2Client") {
    profile =>
      Restrict(Array("botAdmin"), Injectables.handler) {
        Action {
          implicit request =>
            val configDao = Injectables.configDao
            val adminDao = Injectables.adminDao
            val all = configDao.findAll
            val old = all.get(0)

            val form: Form[Config] = configForm.bindFromRequest()

            val config: Config = form.fold(errors => errors.get, form => form)

            if (!old.getOperations.equals(config.getOperations)) {
              val admin = adminDao.getAdmin(request.session.get("userName").get)
              configDao.updateOperations(admin, old.getOperations, config.getOperations)
            }
            var url = config.getUrl
            while (url.endsWith("/")) {
              url = url.substring(0, url.length - 1)
            }
            config.setUrl(url)
            configDao.save(config)
            Redirect("/index")
        }
      }
  }

  //  @Get("/javadoc")
  //  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  def javadoc = Restrict(Array("botAdmin"), Injectables.handler) {
    Action {
      implicit request =>
        Ok(views.html.admin.javadoc(Injectables.handler, Injectables.context(request), Injectables.apiDao.findAll))
    }
  }

  //  @Post("/addJavadoc")
  //  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  /*(name: String, packages: String, baseUrl: String, file: File)*/
  def addJavadoc() = Restrict(Array("botAdmin"), Injectables.handler) {
    Action {
      implicit request =>
      /*
          val savedFile: File = File.createTempFile("javadoc", ".jar")
          Files.copyFile(file, savedFile)
          val apiDao: ApiDao = Injectables.apiDao
          apiDao.save(new ApiEvent(apiDao.find(name) == null, AdminController.getTwitterContext.screenName, name,
            packages, baseUrl, savedFile))
      */
        Ok(views.html.admin.javadoc(Injectables.handler, Injectables.context(request), Injectables.apiDao.findAll))
    }
  }

  //  @Get("/deleteApi")
  //  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  def deleteApi(id: ObjectId) = {
    val apiDao = Injectables.apiDao
    val event = new ApiEvent(apiDao.find(id).getName, "")
    apiDao.save(event)
    javadoc
  }

  //  @Post("/addAdmin")
  //  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  def addAdmin() = Restrict(Array("botAdmin"), Injectables.handler) {
    Action {
      implicit request =>
      //    if (!email.isEmpty) {
      //      val dao: AdminDao = Injectables.adminDao
      //      dao.save(new Admin(ircName, hostName, email, getTwitterContext.screenName))
      //    }
        println("addAdmin")
        adminForm.bindFromRequest.fold(
          errors => BadRequest(views.html.admin.admin(Injectables.handler, Injectables.context(request), errors,
            Injectables.adminDao.findAll())),
          adminInfo => {
            save(adminInfo)
            Redirect("/index")
          })

    }
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
      Ok(views.html.admin.editChannel(Injectables.handler, Injectables.context(request), channelForm.fill(new Channel)))
  }

  //  @Get("/showChannel")
  //  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  def showChannel(name: String = null) = Action {
    implicit request =>
      val channel: Channel = Injectables.channelDao.get(name)
      Ok(views.html.admin.editChannel(Injectables.handler, Injectables.context(request), channelForm.fill(channel)))
  }

  //  @Get("/editChannel")
  def editChannel(channel: Channel) = Action {
    implicit request =>
      val fill: Form[ChannelInfo] = channelForm.fill(channel)
      Ok(views.html.admin.editChannel(Injectables.handler, Injectables.context(request), fill))
  }

  //  @Get("/saveChannel")
  //  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  def saveChannel = Action {
    implicit request =>
      val channelInfo = channelForm.bindFromRequest.get
      save(channelInfo)
      Ok(views.html.admin.config(Injectables.handler, Injectables.context(request), Injectables.config,
        Injectables.operations))
  }

  def save(channelInfo: ChannelInfo) {
    val dao: ChannelDao = Injectables.channelDao
    val channel = if (channelInfo.id == null) {
      dao.find(channelInfo.id)
    } else new Channel
    channel.setName(channelInfo.name)
    channel.setLogged(channelInfo.logged)
    channel.setKey(channelInfo.key)
    dao.save(channel)
  }

  def save(form: AdminForm) {
    val admin = new Admin
    admin.setIrcName(form.ircName.getOrElse(""))
    admin.setHostName(form.hostName.getOrElse(""))
    admin.setUserName(form.email)
    Injectables.adminDao.save(admin)
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
}