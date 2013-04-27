package controllers

import org.joda.time.DateTime
import play.api.mvc.{Action, Controller}
import utils.Injectables
import org.joda.time.format.DateTimeFormat

object Application extends Controller {
  def index = Action {
    implicit request =>
      Ok(views.html.index(Injectables.handler, Injectables.context))
  }

  def factoids = TODO

  def changes = TODO

  def karma = TODO

  def logs(channel: String, dateString: String) = Action {
    implicit request =>
      println("name = " + channel)
      println("dateString = " + dateString)
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
      println(context.logs)

      val before = date.minusDays(1).toString(pattern)
      val after = date.plusDays(1).toString(pattern)
      Ok(views.html.logs(Injectables.handler, context, channel, date.toString(pattern), before, after))
  }
}