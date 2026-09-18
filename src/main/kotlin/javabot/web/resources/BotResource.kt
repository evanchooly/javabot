package javabot.web.resources

import io.quarkus.qute.TemplateInstance
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.CookieParam
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.Cookie
import jakarta.ws.rs.core.MediaType
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javabot.model.Factoid
import javabot.web.JavabotConfiguration
import javabot.web.views.TemplateService
import org.slf4j.LoggerFactory

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
class BotResource @Inject constructor(var templateService: TemplateService) {

    @GET
    @Produces("text/html;charset=ISO-8859-1")
    fun index(
        @CookieParam(JavabotConfiguration.SESSION_TOKEN_NAME) sessionCookie: Cookie?,
        @QueryParam("test.exception") testException: String?,
    ): TemplateInstance {
        if (testException != null) {
            throw RuntimeException("Testing 500 pages")
        }
        return templateService.createIndexView(sessionCookie?.value)
    }

    @GET
    @Path("/index")
    @Produces("text/html;charset=ISO-8859-1")
    fun indexHtml(
        @CookieParam(JavabotConfiguration.SESSION_TOKEN_NAME) sessionCookie: Cookie?,
        @QueryParam("test.exception") testException: String?,
    ): TemplateInstance {
        return index(sessionCookie, testException)
    }

    @GET
    @Path("/factoids")
    @Produces("text/html;charset=ISO-8859-1")
    fun factoids(
        @CookieParam(JavabotConfiguration.SESSION_TOKEN_NAME) sessionCookie: Cookie?,
        @QueryParam("page") page: Int?,
        @QueryParam("name") name: String?,
        @QueryParam("value") value: String?,
        @QueryParam("userName") userName: String?,
    ): TemplateInstance {
        return templateService.createFactoidsView(
            sessionCookie?.value,
            page ?: 1,
            Factoid.of(name, value, userName),
        )
    }

    @GET
    @Path("/karma")
    @Produces("text/html;charset=ISO-8859-1")
    fun karma(
        @CookieParam(JavabotConfiguration.SESSION_TOKEN_NAME) sessionCookie: Cookie?,
        @QueryParam("page") page: Int?,
        @Suppress("UNUSED_PARAMETER") @QueryParam("name") name: String?,
        @Suppress("UNUSED_PARAMETER") @QueryParam("value") value: Int?,
        @Suppress("UNUSED_PARAMETER") @QueryParam("userName") userName: String?,
    ): TemplateInstance {
        return templateService.createKarmaView(sessionCookie?.value, page ?: 1)
    }

    @GET
    @Path("/changes")
    @Produces("text/html;charset=ISO-8859-1")
    fun changes(
        @CookieParam(JavabotConfiguration.SESSION_TOKEN_NAME) sessionCookie: Cookie?,
        @QueryParam("page") page: Int?,
        @QueryParam("message") message: String?,
    ): TemplateInstance {
        return templateService.createChangesView(sessionCookie?.value, page ?: 1, message, null)
    }

    @GET
    @Path("/logs/{channel}/{date}")
    @Produces("text/html;charset=ISO-8859-1")
    fun logs(
        @CookieParam(JavabotConfiguration.SESSION_TOKEN_NAME) sessionCookie: Cookie?,
        @PathParam("channel") channel: String?,
        @PathParam("date") dateString: String?,
    ): TemplateInstance {
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

        return templateService.createLogsView(sessionCookie?.value, channelName, date)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(BotResource::class.java)
        private val PATTERN = "yyyy-MM-dd"

        val FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN)
    }
}
