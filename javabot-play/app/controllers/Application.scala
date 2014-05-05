package controllers

import java.net.{URLDecoder, URLEncoder}
import java.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import play.api.data.Forms._
import play.api.data.Forms.mapping
import play.api.data._
import play.api.mvc._
import utils._
import javax.inject.{Singleton, Inject}
import javabot.dao.AdminDao
import security.OAuthDeadboltHandler
import com.google.inject.Provider
import play.api.mvc
import models.ChangeForm
import models.Page
import models.AdminForm
import models.FactoidForm
import models.KarmaForm

@Singleton
class Application @Inject()(adminDao: AdminDao, handler: OAuthDeadboltHandler, contextProvider: Provider[Context],
                            factoidDao: FactoidDao, changeDao: ChangeDao, karmaDao: KarmaDao) extends Controller {

  val factoidForm: Form[FactoidForm] = Form(
    mapping(
      "name" -> optional(text),
      "value" -> optional(text),
      "user" -> optional(text)
    )(FactoidForm.apply)(FactoidForm.unapply))

  val changeForm: Form[ChangeForm] = Form(
    mapping(
      "message" -> optional(text)
    )(ChangeForm.apply)(ChangeForm.unapply))

  val karmaForm: Form[KarmaForm] = Form(
    mapping(
      "nick" -> optional(text)
    )(KarmaForm.apply)(KarmaForm.unapply))

  val PerPageCount = 50

  def index = Action {
    implicit request =>
      Ok(views.html.index(handler, fillContext(request)))
  }

  def factoids = Action {
    implicit request =>

      val page = request.getQueryString("page")
      val form = factoidForm.bindFromRequest.fold(errors => errors.get, form => form)
      val pageNumber = page.getOrElse("0").toInt
      val pair = factoidDao.find(form, pageNumber * PerPageCount, PerPageCount)
      val content = Page(pair._2, pageNumber, pageNumber * PerPageCount, pair._1)

      val context = contextProvider.get
      context.request(request)

      Ok(views.html.factoids(handler, context, factoidForm.fill(form), content))
  }

  def fillContext(request: mvc.Request[AnyContent]) = {
    val context = contextProvider.get
    context.request(request)
    context
  }

  def karma = Action {
    implicit request =>

      val page = request.getQueryString("page")
      val form = karmaForm.bindFromRequest.fold(errors => errors.get, form => form)
      val pageNumber = page.getOrElse("0").toInt
      val pair = karmaDao.find(pageNumber * PerPageCount, PerPageCount)
      val pageContent = Page(pair._2, pageNumber, pageNumber * PerPageCount, pair._1)

      val fill = karmaForm.fill(form)

      Ok(views.html.karma(handler, fillContext(request), fill, pageContent))
  }

  def changes = Action {
    implicit request =>
      val form = changeForm.bindFromRequest.fold(errors => errors.get, form => form)

      val page = request.getQueryString("page")
      val pageNumber = page.getOrElse("0").toInt
      val pair = changeDao.find(form, pageNumber * PerPageCount, PerPageCount)
      val content = Page(pair._2, pageNumber, pageNumber * PerPageCount, pair._1)

      Ok(views.html.changes(handler, fillContext(request), changeForm.fill(form), content))
  }

  def logs(channel: String, dateString: String) = Action {
    implicit request =>
      var date: DateTime = null
      val pattern = "yyyy-MM-dd"
      val format = DateTimeFormat.forPattern(pattern)

      val channelName = URLDecoder.decode(channel, "UTF-8")
      try {
        if ("today".equals(dateString)) {
          date = DateTime.now().withTimeAtStartOfDay()
        } else {
          date = DateTime.parse(dateString, format)
        }
      } catch {
        case e: Exception => {
          date = DateTime.now().withTimeAtStartOfDay()
        }
      }

      //      date = date.withZoneRetainFields(DateTimeZone.forID("US/Eastern"))
      val context = fillContext(request)
      context.logChannel(channelName, date)

      val before = date.minusDays(1).toString(pattern)
      val after = date.plusDays(1).toString(pattern)
      Ok(views.html.logs(handler, context, channelName, date.toString(pattern), before, after))
  }
}

object Encodings {
  def encodeForm[T](url: String, form: Form[T]) = {
    form.data.foldLeft(url) {
      (s: String, pair: (String, String)) =>
        s + "&" + URLEncoder.encode(pair._1, "UTF-8") + "=" + URLEncoder.encode(pair._2, "UTF-8")
    }
  }
}