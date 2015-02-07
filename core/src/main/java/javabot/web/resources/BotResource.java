package javabot.web.resources;

import com.google.inject.Injector;
import io.dropwizard.views.View;
import javabot.model.Change;
import javabot.model.Factoid;
import javabot.web.views.ChangesView;
import javabot.web.views.FactoidsView;
import javabot.web.views.IndexView;
import javabot.web.views.KarmaView;
import javabot.web.views.LogsView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BotResource {
    private static final Logger LOG = LoggerFactory.getLogger(BotResource.class);
    private static final String PATTERN = "yyyy-MM-dd";

    public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern(PATTERN);

    @Inject
    private Injector injector;

    @GET
    @Produces("text/html;charset=ISO-8859-1")
    public View index(@Context HttpServletRequest request) {
        if (request.getParameter("test.exception") != null) {
            throw new RuntimeException("Testing 500 pages");
        }
        return new IndexView(BotResource.this.injector, request);
    }

    @GET
    @Path("/index")
    @Produces("text/html;charset=ISO-8859-1")
    public View indexHtml(@Context HttpServletRequest request) {
        return index(request);
    }

    @GET
    @Path("/factoids")
    @Produces("text/html;charset=ISO-8859-1")
    public View factoids(@Context HttpServletRequest request, @QueryParam("page") Integer page,
                         @QueryParam("name") String name, @QueryParam("value") String value, @QueryParam("userName") String userName) {
        return new FactoidsView(BotResource.this.injector, request, page == null ? 1 : page, new Factoid(name, value, userName));
    }

    @GET
    @Path("/karma")
    @Produces("text/html;charset=ISO-8859-1")
    public View karma(@Context HttpServletRequest request, @QueryParam("page") Integer page,
                      @QueryParam("name") String name, @QueryParam("value") Integer value, @QueryParam("userName") String userName) {
        return new KarmaView(BotResource.this.injector, request, page == null ? 1 : page);
    }

    @GET
    @Path("/changes")
    @Produces("text/html;charset=ISO-8859-1")
    public View changes(@Context HttpServletRequest request, @QueryParam("page") Integer page, @QueryParam("message") String message) {
        return new ChangesView(BotResource.this.injector, request, page == null ? 1 : page, new Change(message));
    }

    @GET
    @Path("/logs/{channel}/{date}")
    @Produces("text/html;charset=ISO-8859-1")
    public View logs(@Context HttpServletRequest request, @PathParam("channel") String channel, @PathParam("date") String dateString) {
        LocalDateTime date;
        String channelName;
        try {
            channelName = URLDecoder.decode(channel, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
        try {
            if ("today".equals(dateString)) {
                date = LocalDate.now().atStartOfDay();
            } else {
                date = LocalDate.parse(dateString, FORMAT).atStartOfDay();
            }
        } catch (Exception e) {
            date = LocalDate.now().atStartOfDay();
        }

        return new LogsView(injector, request, channelName, date);
    }

}
