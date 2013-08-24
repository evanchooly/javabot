package controllers

import be.objectify.deadbolt.scala.DeadboltActions
import javabot.dao.{ApiDao, ChannelDao}
import javabot.model._
import org.bson.types.ObjectId
import org.pac4j.play.scala.ScalaController
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import scala.collection.JavaConversions._
import utils.Implicits._
import utils.{FactoidDao, AdminDao, ConfigDao, Context}
import models.{ApiForm, ConfigInfo, AdminForm, ChannelInfo}
import play.api.mvc
import com.google.inject.Provider
import security.OAuthDeadboltHandler
import java.io.File
import javax.inject.{Singleton, Inject}

@Singleton
class AdminController @Inject()(configDao: ConfigDao, adminDao: AdminDao, factoidDao: FactoidDao, apiDao: ApiDao,
                                channelDao: ChannelDao, contextProvider: Provider[Context],
                                handler: OAuthDeadboltHandler) extends ScalaController with DeadboltActions {

  val adminForm: Form[AdminForm] = Form(
    mapping(
      "ircName" -> text,
      "hostName" -> optional(text),
      "email" -> text
    )(AdminForm.apply)(AdminForm.unapply))
  val apiForm: Form[ApiForm] = Form(
    mapping(
      "name" -> text,
      "baseUrl" -> text
    )(ApiForm.apply)(ApiForm.unapply))
  val channelForm: Form[ChannelInfo] = Form(
    mapping(
      "id" -> of[ObjectId],
      "name" -> nonEmptyText,
      "key" -> text,
      "logged" -> boolean
    )(ChannelInfo.apply)(ChannelInfo.unapply))
  val configForm: Form[ConfigInfo] = Form(
    mapping(
      "server" -> text,
      "url" -> text,
      "port" -> number,
      "historyLength" -> number
        .verifying("History length must be greater than zero", length => length > 0),
      "trigger" -> text,
      "nick" -> text,
      "password" -> text
    )(ConfigInfo.apply)(ConfigInfo.unapply))

  def fillContext(request: mvc.Request[AnyContent]) = {
    val context = contextProvider.get
    context.request(request)
    context
  }

  def login = RequiresAuthentication("Google2Client") {
    profile =>
      Action {
        implicit request =>
          val dao: utils.AdminDao = adminDao
          if (dao.findAll().isEmpty) {
            dao.create("", profile.getEmail, "")
          }
          Ok(views.html.admin.admin(handler, fillContext(request), adminForm.bindFromRequest(), adminDao.findAll()))
            .withSession("userName" -> profile.getEmail)
      }
  }

  def index = RequiresAuthentication("Google2Client") {
    profile =>
      Action {
        implicit request =>
          Ok(views.html.admin.admin(handler, fillContext(request), adminForm.bindFromRequest(), adminDao.findAll()))
      }
  }

  def showConfig = Restrict(Array("botAdmin"), handler) {
    Action {
      implicit request =>
        Ok(views.html.admin.config(handler, fillContext(request),
          buildConfigForm, configDao.get.getOperations.toSet, configDao.operations))
    }
  }

  def saveConfig = RequiresAuthentication("Google2Client") {
    profile =>
      Restrict(Array("botAdmin"), handler) {
        Action {
          implicit request =>
            val admin = adminDao.getAdmin(request.session.get("userName").get)

            val form: Form[ConfigInfo] = configForm.bindFromRequest()

            form.fold(
              errors => {
                BadRequest(views.html.admin.config(handler, fillContext(request),
                  errors, configDao.get.getOperations.toSet, configDao.operations))
              },
              configInfo => {
                configDao.save(admin, configInfo)
                Redirect(routes.Application.index())
              })

        }
      }
  }

  def enableOperation(name: String) = RequiresAuthentication("Google2Client") {
    profile =>
      Restrict(Array("botAdmin"), handler) {
        Action {
          implicit request =>
            adminDao.enableOperation(name, getAdmin(request.session).getUserName)

            Redirect(routes.AdminController.showConfig())
        }
      }
  }

  def disableOperation(name: String) = RequiresAuthentication("Google2Client") {
    profile =>
      Restrict(Array("botAdmin"), handler) {
        Action {
          implicit request =>
            adminDao.disableOperation(name, getAdmin(request.session).getUserName)

            Redirect(routes.AdminController.showConfig())
        }
      }
  }

  def getAdmin(session: Session): Admin = {
    adminDao.getAdmin(session.get("userName").get)
  }

  def javadoc = RequiresAuthentication("Google2Client") {
    profile =>
      Restrict(Array("botAdmin"), handler) {
        Action {
          implicit request => {
            Ok(views.html.admin.javadoc(handler, fillContext(request), apiDao.findAll))
          }
        }
      }
  }

  def FileUploadAction[A](bp: BodyParser[A])(f: Request[A] => Result): Action[A] = {
    Action(bp) { request =>
      f(request)
    }
  }

  def addApi = Restrict(Array("botAdmin"), handler) {
    FileUploadAction(parse.multipartFormData) {
      implicit request => {
        val map: Option[mvc.SimpleResult[mvc.Results.EmptyContent]] = request.body.file("file").map {
          jar =>
            val form = apiForm.bindFromRequest().get
            val savedFile = File.createTempFile(form.name + "javadoc", ".jar")
            jar.ref.moveTo(savedFile, replace = true)
            apiDao.save(new ApiEvent(apiDao.find(form.name) == null, getAdmin(request.session).getUserName,
              form.name, form.baseUrl, savedFile))
            Redirect(routes.AdminController.javadoc())
        }
        map.get
      }
    }
  }

  def deleteApi(id: ObjectId) = RequiresAuthentication("Google2Client") {
    profile =>
      Restrict(Array("botAdmin"), handler) {
        Action {
          implicit request => {
            val event = new ApiEvent(apiDao.find(id).getName, getAdmin(request.session).getUserName)
            apiDao.save(event)
            Redirect(routes.AdminController.javadoc())
          }
        }
      }
  }

  def addAdmin() = RequiresAuthentication("Google2Client") {
    profile =>
      Restrict(Array("botAdmin"), handler) {
        Action {
          implicit request => {
            adminForm.bindFromRequest.fold(
              errors => BadRequest(views.html.admin.admin(handler, fillContext(request), errors, adminDao.findAll())),
              adminInfo => {
                save(adminInfo)
                Redirect(routes.AdminController.index())
              })

          }
        }
      }
  }

  //  @Get("/deleteAdmin")
  //  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  def deleteAdmin(id: ObjectId) = RequiresAuthentication("Google2Client") {
    profile =>
      Restrict(Array("botAdmin"), handler) {
        Action {
          implicit request => {
            val admin = adminDao.find(id)
            if (admin != null && !admin.getBotOwner) {
              adminDao.delete(admin)
            }
            Redirect(routes.AdminController.index())
          }
        }
      }
  }

  //  @Post("/updateAdmin")
  //  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  def updateAdmin(userName: String, ircName: String) {
    val dao = adminDao
    val admin = dao.getAdmin(userName)
    admin.setIrcName(ircName)
    dao.save(admin)
    index
  }

  //  @Get("/addChannel")
  //  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  def addChannel() = Action {
    implicit request =>
      Ok(views.html.admin.editChannel(handler, fillContext(request), channelForm.fill(new Channel)))
  }

  //  @Get("/showChannel")
  //  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  def showChannel(name: String = null) = Action {
    implicit request =>
      val channel = channelDao.get(name)
      Ok(views.html.admin.editChannel(handler, fillContext(request), channelForm.fill(channel)))
  }

  //  @Get("/editChannel")
  def editChannel(channel: Channel) = Action {
    implicit request =>
      val fill: Form[ChannelInfo] = channelForm.fill(channel)
      Ok(views.html.admin.editChannel(handler, fillContext(request), fill))
  }

  //  @Get("/saveChannel")
  //  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  def saveChannel = Action {
    implicit request =>
      val channelInfo = channelForm.bindFromRequest.get
      save(channelInfo)
      Ok(views.html.admin.config(handler, fillContext(request), buildConfigForm,
        configDao.get.getOperations.toSet, configDao.operations))
  }

  def buildConfigForm = {
    val config: Config = configDao.get
    val info: ConfigInfo = ConfigInfo(config.getServer, config.getUrl, config.getPort, config.getHistoryLength,
      config.getTrigger,
      config.getNick, config.getPassword)
    configForm.fill(info)
  }

  def save(channelInfo: ChannelInfo) {
    val channel = if (channelInfo.id == null) {
      channelDao.find(channelInfo.id)
    } else {
      new Channel
    }
    channel.setName(channelInfo.name)
    channel.setLogged(channelInfo.logged)
    channel.setKey(channelInfo.key)
    channelDao.save(channel)
  }

  def save(form: AdminForm) {
    val admin = new Admin
    admin.setIrcName(form.ircName)
    admin.setHostName(form.hostName.getOrElse(""))
    admin.setUserName(form.email)
    adminDao.save(admin)
  }

  //  @Get("/toggleLock")
  //  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  def toggleLock(id: ObjectId) = Action {
    implicit request =>
      val factoid: Factoid = factoidDao.find(id)
      factoid.setLocked(!factoid.getLocked)
      factoidDao.save(factoid)
      request.body.asText.map {
        text =>
          Ok(factoid.getLocked.toString)
      }.getOrElse {
        BadRequest("Expecting text/plain request body")
      }

  }
}