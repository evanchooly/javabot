package javabot.kotlin.web.resources

import com.google.inject.Injector
import io.dropwizard.views.View
import javabot.kotlin.web.views.ChangesView
import javabot.kotlin.web.views.FactoidsView
import javabot.kotlin.web.views.IndexView
import javabot.kotlin.web.views.KarmaView
import javabot.kotlin.web.views.LogsView
import javabot.model.Change
import javabot.model.Factoid
import org.slf4j.LoggerFactory
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.servlet.http.HttpServletRequest
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BotResource {

    @Inject
    private lateinit val injector: Injector

    @GET
    @Produces("text/html;charset=ISO-8859-1")
    public fun index(@Context request: HttpServletRequest): View {
        if (request.getParameter("test.exception") != null) {
            throw RuntimeException("Testing 500 pages")
        }
        return IndexView(injector, request)
    }

    @GET
    @Path("/index")
    @Produces("text/html;charset=ISO-8859-1")
    public fun indexHtml(@Context request: HttpServletRequest): View {
        return index(request)
    }

    @GET
    @Path("/factoids")
    @Produces("text/html;charset=ISO-8859-1")
    public fun factoids(@Context request: HttpServletRequest, @QueryParam("page") page: Int?,
                        @QueryParam("name") name: String, @QueryParam("value") value: String, @QueryParam("userName") userName: String): View {
        return FactoidsView(this@BotResource.injector, request, page ?: 1, Factoid(name, value, userName))
    }

    @GET
    @Path("/karma")
    @Produces("text/html;charset=ISO-8859-1")
    public fun karma(@Context request: HttpServletRequest, @QueryParam("page") page: Int?,
                     @QueryParam("name") name: String, @QueryParam("value") value: Int?, @QueryParam("userName") userName: String): View {
        return KarmaView(this@BotResource.injector, request, page ?: 1)
    }

    @GET
    @Path("/changes")
    @Produces("text/html;charset=ISO-8859-1")
    public fun changes(@Context request: HttpServletRequest, @QueryParam("page") page: Int?, @QueryParam("message") message: String): View {
        return ChangesView(this@BotResource.injector, request, page ?: 1, Change(message))
    }

    @GET
    @Path("/logs/{channel}/{date}")
    @Produces("text/html;charset=ISO-8859-1")
    public fun logs(@Context request: HttpServletRequest, @PathParam("channel") channel: String, @PathParam("date") dateString: String): View {
        val date: LocalDateTime
        val channelName: String
        try {
            channelName = URLDecoder.decode(channel, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            LOG.error(e.getMessage(), e)
            throw RuntimeException(e.getMessage(), e)
        }

        try {
            if ("today" == dateString) {
                date = LocalDate.now().atStartOfDay()
            } else {
                date = LocalDate.parse(dateString, FORMAT).atStartOfDay()
            }
        } catch (e: Exception) {
            date = LocalDate.now().atStartOfDay()
        }

        return LogsView(injector, request, channelName, date)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(BotResource::class.java)
        private val PATTERN = "yyyy-MM-dd"

        public val FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN)
    }

}
