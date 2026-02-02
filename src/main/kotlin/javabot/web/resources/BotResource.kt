package javabot.web.resources

import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javabot.model.Factoid
import javabot.web.views.ViewFactory
import javax.inject.Inject
import org.slf4j.LoggerFactory

class BotResource @Inject constructor(var viewFactory: ViewFactory) {

    fun configureRoutes(routing: Routing) {
        routing {
            get("/") {
                if (call.request.queryParameters["test.exception"] != null) {
                    throw RuntimeException("Testing 500 pages")
                }
                val view = viewFactory.createIndexView(KtorServletRequest(call))
                call.respond(FreeMarkerContent(view.getChildView(), view.toModel()))
            }

            get("/index") {
                val view = viewFactory.createIndexView(KtorServletRequest(call))
                call.respond(FreeMarkerContent(view.getChildView(), view.toModel()))
            }

            get("/factoids") {
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                val name = call.request.queryParameters["name"]
                val value = call.request.queryParameters["value"]
                val userName = call.request.queryParameters["userName"]
                
                val view = viewFactory.createFactoidsView(
                    KtorServletRequest(call),
                    page,
                    Factoid.of(name, value, userName)
                )
                call.respond(FreeMarkerContent(view.getChildView(), view.toModel()))
            }

            get("/karma") {
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                val view = viewFactory.createKarmaView(KtorServletRequest(call), page)
                call.respond(FreeMarkerContent(view.getChildView(), view.toModel()))
            }

            get("/changes") {
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                val message = call.request.queryParameters["message"]
                val view = viewFactory.createChangesView(KtorServletRequest(call), page, message)
                call.respond(FreeMarkerContent(view.getChildView(), view.toModel()))
            }

            get("/logs/{channel}/{date}") {
                val channel = call.parameters["channel"]
                val dateString = call.parameters["date"]
                
                val date: LocalDateTime =
                    try {
                        if ("today" == dateString) LocalDate.now().atStartOfDay()
                        else LocalDate.parse(dateString, FORMAT).atStartOfDay()
                    } catch (e: Exception) {
                        LocalDate.now().atStartOfDay()
                    }
                val channelName: String
                try {
                    channelName = URLDecoder.decode(channel, "UTF-8")
                } catch (e: UnsupportedEncodingException) {
                    LOG.error(e.message, e)
                    throw RuntimeException(e.message, e)
                }

                val view = viewFactory.createLogsView(KtorServletRequest(call), channelName, date)
                call.respond(FreeMarkerContent(view.getChildView(), view.toModel()))
            }
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(BotResource::class.java)
        private val PATTERN = "yyyy-MM-dd"

        val FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN)
    }
}
