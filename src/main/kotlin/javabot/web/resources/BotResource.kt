package javabot.web.resources

import io.quarkus.qute.TemplateInstance
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.servlet.http.HttpServletRequest
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javabot.model.Factoid
import javabot.web.views.TemplateService
import org.slf4j.LoggerFactory

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
class BotResource @Inject constructor(var templateService: TemplateService) {

    @GET
    @Produces("text/html;charset=ISO-8859-1")
    fun index(@Context request: HttpServletRequest): TemplateInstance {
        if (request.getParameter("test.exception") != null) {
            throw RuntimeException("Testing 500 pages")
        }
        return templateService.createIndexView(request)
    }

    @GET
    @Path("/index")
    @Produces("text/html;charset=ISO-8859-1")
    fun indexHtml(@Context request: HttpServletRequest): TemplateInstance {
        return index(request)
    }

    @GET
    @Path("/factoids")
    @Produces("text/html;charset=ISO-8859-1")
    fun factoids(
        @Context request: HttpServletRequest,
        @QueryParam("page") page: Int?,
        @QueryParam("name") name: String?,
        @QueryParam("value") value: String?,
        @QueryParam("userName") userName: String?,
    ): TemplateInstance {
        return templateService.createFactoidsView(
            request,
            page ?: 1,
            Factoid.of(name, value, userName),
        )
    }

    @GET
    @Path("/karma")
    @Produces("text/html;charset=ISO-8859-1")
    fun karma(
        @Context request: HttpServletRequest,
        @QueryParam("page") page: Int?,
        @Suppress("UNUSED_PARAMETER") @QueryParam("name") name: String?,
        @Suppress("UNUSED_PARAMETER") @QueryParam("value") value: Int?,
        @Suppress("UNUSED_PARAMETER") @QueryParam("userName") userName: String?,
    ): TemplateInstance {
        return templateService.createKarmaView(request, page ?: 1)
    }

    @GET
    @Path("/changes")
    @Produces("text/html;charset=ISO-8859-1")
    fun changes(
        @Context request: HttpServletRequest,
        @QueryParam("page") page: Int?,
        @QueryParam("message") message: String?,
    ): TemplateInstance {
        return templateService.createChangesView(request, page ?: 1, message, null)
    }

    @GET
    @Path("/logs/{channel}/{date}")
    @Produces("text/html;charset=ISO-8859-1")
    fun logs(
        @Context request: HttpServletRequest,
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

        return templateService.createLogsView(request, channelName, date)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(BotResource::class.java)
        private val PATTERN = "yyyy-MM-dd"

        val FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN)
    }
}
