package controllers

import java.net.URLEncoder
import models.{Page, FactoidForm}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.data.Forms._
import play.api.data.Forms.mapping
import play.api.data._
import play.api.mvc._
import utils.{FactoidDao, Injectables}

object Application extends Controller {
  val factoidForm: Form[FactoidForm] = Form(
    mapping(
      "name" -> optional(text),
      "value" -> optional(text),
      "user" -> optional(text)
    )(FactoidForm.apply)(FactoidForm.unapply)
  )

  val PerPageCount = 50

  def encodeForm[T](url: String, form: Form[T]) = {
    form.data.foldLeft(url) {
      (s: String, pair: (String, String)) =>
        s + "&" + URLEncoder.encode(pair._1, "UTF-8") + "=" + URLEncoder.encode(pair._2, "UTF-8")
    }
  }

  def index = Action {
    implicit request =>
      Ok(views.html.index(Injectables.handler, Injectables.context))
  }

  def factoids = Action {
    implicit request =>
      val dao: FactoidDao = Injectables.factoidDao

      val page = request.getQueryString("page")
      val form = factoidForm.bindFromRequest.fold(errors => errors.get, form => form)
      val pageNumber = page.getOrElse("0").toInt
      val pair = dao.find(form, pageNumber * PerPageCount, PerPageCount)
      val content = Page(pair._2, pageNumber, pageNumber * PerPageCount, pair._1)

      val fill = factoidForm.fill(form)
      Ok(views.html.factoids(Injectables.handler, Injectables.context, fill, content))
  }

  def karma = Action {
    implicit request =>

      val page = request.getQueryString("page")
      val pageNumber = page.getOrElse("0").toInt
      val pair = Injectables.karmaDao.find(pageNumber * PerPageCount, PerPageCount)
      val pageContent = Page(pair._2, pageNumber, pageNumber * PerPageCount, pair._1)

      Ok(views.html.karma(Injectables.handler, Injectables.context, pageContent))
  }

  def changes = TODO

  def logs(channel: String, dateString: String) = Action {
    implicit request =>
      var date: DateTime = null
      val pattern = "yyyy-MM-dd"
      val format = DateTimeFormat.forPattern(pattern)

      if ("today".equals(dateString)) {
        date = DateTime.now().withTimeAtStartOfDay()
      } else {
        try {
          date = DateTime.parse(dateString, format)
        } catch {
          case e: Exception => {
            date = DateTime.now().withTimeAtStartOfDay()
          }
        }
      }

      val context = Injectables.context
      context.logChannel(channel, date)

      val before = date.minusDays(1).toString(pattern)
      val after = date.plusDays(1).toString(pattern)
      Ok(views.html.logs(Injectables.handler, context, channel, date.toString(pattern), before, after))
  }
}