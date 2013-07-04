package controllers

import java.net.{URLDecoder, URLEncoder}
import models.{AdminForm, KarmaForm, ChangeForm, Page, FactoidForm}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.data.Forms._
import play.api.data.Forms.mapping
import play.api.data._
import play.api.mvc._
import utils.Injectables

object Application extends Controller {
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

  val adminForm: Form[AdminForm] = Form(
    mapping(
      "ircName" -> optional(text),
      "hostName" -> optional(text),
      "email" -> text
    )(AdminForm.apply)(AdminForm.unapply))

  val PerPageCount = 50

  def encodeForm[T](url: String, form: Form[T]) = {
    form.data.foldLeft(url) {
      (s: String, pair: (String, String)) =>
        s + "&" + URLEncoder.encode(pair._1, "UTF-8") + "=" + URLEncoder.encode(pair._2, "UTF-8")
    }
  }

  def index = Action {
    implicit request =>
      Ok(views.html.index(Injectables.handler, Injectables.context(request)))
  }

  def factoids = Action {
    implicit request =>
      val dao = Injectables.factoidDao

      val page = request.getQueryString("page")
      val form = factoidForm.bindFromRequest.fold(errors => errors.get, form => form)
      val pageNumber = page.getOrElse("0").toInt
      val pair = dao.find(form, pageNumber * PerPageCount, PerPageCount)
      val content = Page(pair._2, pageNumber, pageNumber * PerPageCount, pair._1)

      val fill = factoidForm.fill(form)
      Ok(views.html.factoids(Injectables.handler, Injectables.context(request), fill, content))
  }

  def karma = Action {
    implicit request =>

      val page = request.getQueryString("page")
      val form = karmaForm.bindFromRequest.fold(errors => errors.get, form => form)
      val pageNumber = page.getOrElse("0").toInt
      val pair = Injectables.karmaDao.find(pageNumber * PerPageCount, PerPageCount)
      val pageContent = Page(pair._2, pageNumber, pageNumber * PerPageCount, pair._1)

      val fill = karmaForm.fill(form)
      Ok(views.html.karma(Injectables.handler, Injectables.context(request), fill, pageContent))
  }

  def changes = Action {
    implicit request =>
      val dao = Injectables.changeDao
      val form = changeForm.bindFromRequest.fold(errors => errors.get, form => form)

      val page = request.getQueryString("page")
      val pageNumber = page.getOrElse("0").toInt
      val pair = dao.find(form, pageNumber * PerPageCount, PerPageCount)
      val content = Page(pair._2, pageNumber, pageNumber * PerPageCount, pair._1)

      Ok(views.html.changes(Injectables.handler, Injectables.context(request), changeForm.fill(form), content))
  }

  def logs(channel: String, dateString: String) = Action {
    implicit request =>
      var date: DateTime = null
      val pattern = "yyyy-MM-dd"
      val format = DateTimeFormat.forPattern(pattern)

      val channelName = URLDecoder.decode(channel, "UTF-8")
      if ("today".equals(dateString)) {
        date = DateTime.now().withTimeAtStartOfDay()
      } else {
        try {
          date = DateTime.parse(dateString, format)
        } catch {
          case e: Exception => {
            println("error!")
            date = DateTime.now().withTimeAtStartOfDay()
          }
        }
      }

      //      date = date.withZoneRetainFields(DateTimeZone.forID("US/Eastern"))
      val context = Injectables.context(request)
      context.logChannel(channelName, date)

      val before = date.minusDays(1).toString(pattern)
      val after = date.plusDays(1).toString(pattern)
      Ok(views.html.logs(Injectables.handler, context, channelName, date.toString(pattern), before, after))
  }
}