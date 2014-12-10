package javabot.web.resources;

import com.google.inject.Injector;
import io.dropwizard.views.View;
import io.dropwizard.views.freemarker.FreemarkerViewRenderer;
import javabot.Javabot;
import javabot.dao.AdminDao;
import javabot.dao.ApiDao;
import javabot.dao.ChannelDao;
import javabot.dao.ConfigDao;
import javabot.model.Admin;
import javabot.model.ApiEvent;
import javabot.model.Channel;
import javabot.model.Config;
import javabot.model.EventType;
import javabot.web.auth.Restricted;
import javabot.web.model.Authority;
import javabot.web.model.User;
import javabot.web.views.AdminIndexView;
import javabot.web.views.ChannelEditView;
import javabot.web.views.ConfigurationView;
import javabot.web.views.JavadocView;
import org.bson.types.ObjectId;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.URI;

@Path("/admin")
public class AdminResource {
    @Inject
    private Injector injector;

    @Inject
    private AdminDao adminDao;

    @Inject
    private ApiDao apiDao;

    @Inject
    private ConfigDao configDao;

    @Inject
    private ChannelDao channelDao;

    @Inject
    private Javabot javabot;

    @GET
    public View index(@Context HttpServletRequest request, @Restricted(Authority.ROLE_ADMIN) User user) {
        return new AdminIndexView(injector, request);
    }

    @GET
    @Path("/config")
    public View config(@Context HttpServletRequest request, @Restricted(Authority.ROLE_ADMIN) User user) {
        return new ConfigurationView(injector, request);
    }

    @GET
    @Path("/javadoc")
    public View javadoc(@Context HttpServletRequest request, @Restricted(Authority.ROLE_ADMIN) User user) {
        return new JavadocView(injector, request);
    }

    @GET
    @Path("/newChannel")
    public View newChannel(@Context HttpServletRequest request, @Restricted(Authority.ROLE_ADMIN) User user) {
        return new ChannelEditView(injector, request, new Channel());
    }

    @POST
    @Path("/saveConfig")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public View saveConfig(@Context HttpServletRequest request, @Restricted(Authority.ROLE_ADMIN) User user,
                           @FormParam("server") String server, @FormParam("url") String url, @FormParam("port") Integer port,
                           @FormParam("historyLength") Integer historyLength, @FormParam("trigger") String trigger,
                           @FormParam("nick") String nick, @FormParam("password") String password,
                           @FormParam("throttleThreshold") Integer throttleThreshold,
                           @FormParam("minimumNickServAge") Integer minimumNickServAge) {
        Config config = configDao.get();
        config.setServer(server);
        config.setUrl(url);
        config.setPort(port);
        config.setHistoryLength(historyLength);
        config.setTrigger(trigger);
        config.setNick(nick);
        config.setPassword(password);
        config.setThrottleThreshold(throttleThreshold);
        config.setMinimumNickServAge(minimumNickServAge);
        configDao.save(config);
        return new ConfigurationView(injector, request);
    }

    @GET
    @Path("/enableOperation/{name}")
    public View enableOperation(@Context HttpServletRequest request, @Restricted(Authority.ROLE_ADMIN) User user,
                                @PathParam("name") String name) {
        javabot.enableOperation(name);
        return new ConfigurationView(injector, request);
    }

    @GET
    @Path("/disableOperation/{name}")
    public View disableOperation(@Context HttpServletRequest request, @Restricted(Authority.ROLE_ADMIN) User user,
                                 @PathParam("name") String name) {
        javabot.disableOperation(name);
        return new ConfigurationView(injector, request);
    }


    @GET
    @Path("/delete/{id}")
    public View deleteAdmin(@Context HttpServletRequest request, @Restricted(Authority.ROLE_ADMIN) User user,
                            @PathParam("id") String id) {
        Admin admin = adminDao.find(new ObjectId(id));
        if (admin != null && !admin.getBotOwner()) {
            adminDao.delete(admin);
        }
        return index(request, user);
    }

    @POST
    @Path("/add")
    public View addAdmin(@Context HttpServletRequest request, @Restricted(Authority.ROLE_ADMIN) User user,
                         @FormParam("ircName") String ircName, @FormParam("hostName") String hostName,
                         @FormParam("emailAddress") String emailAddress) {
        Admin admin = adminDao.getAdminByEmailAddress(emailAddress);
        if (admin == null) {
            admin = new Admin();
            admin.setIrcName(ircName);
            admin.setHostName(hostName);
            admin.setEmailAddress(emailAddress);
            adminDao.save(admin);
        }
        return index(request, user);
    }

    @POST
    @Path("/addApi")
    public View addApi(@Context HttpServletRequest request, @Restricted(Authority.ROLE_ADMIN) User user,
                       @FormParam("name") String name, @FormParam("baseUrl") String baseUrl,
                       @FormParam("downloadUrl") String downloadUrl) {
        apiDao.save(new ApiEvent(user.getEmail(), name, baseUrl, downloadUrl));
        return index(request, user);
    }

    @GET
    @Path("/deleteApi/{id}")
    public View deleteApi(@Context HttpServletRequest request,
                          @Restricted(Authority.ROLE_ADMIN) User user,
                          @PathParam("id") String id) {
        apiDao.save(new ApiEvent(EventType.DELETE, user.getEmail(), new ObjectId(id)));
        return index(request, user);
    }

    @POST
    @Path("/saveChannel")
    public View saveChannel(@Context HttpServletRequest request, @Restricted(Authority.ROLE_ADMIN) User user,
                            @FormParam("id") String id, @FormParam("name") String name, @FormParam("key") String key,
                            @FormParam("logged") boolean logged) {
        Channel channel = id == null ? new Channel(name, key, logged) : new Channel(new ObjectId(id), name, key, logged);
        channelDao.save(channel);
        return index(request, user);
    }

}
