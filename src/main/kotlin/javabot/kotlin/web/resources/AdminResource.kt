package javabot.kotlin.web.resources

import com.google.inject.Injector
import io.dropwizard.views.View
import javabot.Javabot
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javabot.dao.ChannelDao
import javabot.dao.ConfigDao
import javabot.kotlin.web.model.Authority
import javabot.kotlin.web.auth.Restricted
import javabot.kotlin.web.model.User
import javabot.kotlin.web.views.AdminIndexView
import javabot.kotlin.web.views.ChannelEditView
import javabot.kotlin.web.views.ConfigurationView
import javabot.kotlin.web.views.JavadocView
import javabot.model.Admin
import javabot.model.ApiEvent
import javabot.model.Channel
import javabot.model.EventType
import org.bson.types.ObjectId
import javax.inject.Inject
import javax.servlet.http.HttpServletRequest
import javax.ws.rs.Consumes
import javax.ws.rs.FormParam
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType

@Path("/admin")
public class AdminResource {
    @Inject
    private lateinit val injector: Injector

    @Inject
    private lateinit val adminDao: AdminDao

    @Inject
    private lateinit val apiDao: ApiDao

    @Inject
    private lateinit val configDao: ConfigDao

    @Inject
    private lateinit val channelDao: ChannelDao

    @Inject
    private lateinit val javabot: Javabot

    @GET
    public fun index(@Context request: HttpServletRequest, @Restricted(Authority.ROLE_ADMIN) user: User): View {
        return AdminIndexView(injector, request)
    }

    @GET
    @Path("/config")
    fun config(@Context request: HttpServletRequest, @Restricted(Authority.ROLE_ADMIN) user: User): View {
        return ConfigurationView(injector, request)
    }

    @GET
    @Path("/javadoc")
    fun javadoc(@Context request: HttpServletRequest, @Restricted(Authority.ROLE_ADMIN) user: User): View {
        return JavadocView(injector, request)
    }

    @GET
    @Path("/newChannel")
    fun newChannel(@Context request: HttpServletRequest,
                          @Restricted(Authority.ROLE_ADMIN) user: User): View {
        return ChannelEditView(injector, request, Channel())
    }

    @GET
    @Path("/editChannel/{channel}")
    public fun editChannel(@Context request: HttpServletRequest, @Restricted(Authority.ROLE_ADMIN) user: User,
                           @PathParam("channel") channel: String): View {

        return ChannelEditView(injector, request, channelDao.get(channel))
    }

    @POST
    @Path("/saveConfig")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public fun saveConfig(@Context request: HttpServletRequest, @Restricted(Authority.ROLE_ADMIN) user: User,
                          @FormParam("server") server: String, @FormParam("url") url: String, @FormParam("port") port: Int?,
                          @FormParam("historyLength") historyLength: Int?, @FormParam("trigger") trigger: String,
                          @FormParam("nick") nick: String, @FormParam("password") password: String,
                          @FormParam("throttleThreshold") throttleThreshold: Int?,
                          @FormParam("minimumNickServAge") minimumNickServAge: Int?): View {
        val config = configDao.get()
        config.server = server
        config.url = url
        config.port = port
        config.historyLength = historyLength
        config.trigger = trigger
        config.nick = nick
        config.password = password
        config.throttleThreshold = throttleThreshold
        config.minimumNickServAge = minimumNickServAge
        configDao.save(config)
        return ConfigurationView(injector, request)
    }

    @GET
    @Path("/enableOperation/{name}")
    public fun enableOperation(@Context request: HttpServletRequest, @Restricted(Authority.ROLE_ADMIN) user: User,
                               @PathParam("name") name: String): View {
        javabot.enableOperation(name)
        return ConfigurationView(injector, request)
    }

    @GET
    @Path("/disableOperation/{name}")
    public fun disableOperation(@Context request: HttpServletRequest, @Restricted(Authority.ROLE_ADMIN) user: User,
                                @PathParam("name") name: String): View {
        javabot.disableOperation(name)
        return ConfigurationView(injector, request)
    }


    @GET
    @Path("/delete/{id}")
    public fun deleteAdmin(@Context request: HttpServletRequest, @Restricted(Authority.ROLE_ADMIN) user: User,
                           @PathParam("id") id: String): View {
        val admin = adminDao.find(ObjectId(id))
        if (admin != null && (!admin.botOwner)) {
            adminDao.delete(admin)
        }
        return index(request, user)
    }

    @POST
    @Path("/add")
    public fun addAdmin(@Context request: HttpServletRequest, @Restricted(Authority.ROLE_ADMIN) user: User,
                        @FormParam("ircName") ircName: String, @FormParam("hostName") hostName: String,
                        @FormParam("emailAddress") emailAddress: String): View {
        var admin: Admin? = adminDao.getAdminByEmailAddress(emailAddress)
        if (admin == null) {
            admin = Admin()
            admin.ircName = ircName
            admin.hostName = hostName
            admin.emailAddress = emailAddress
            adminDao.save(admin)
        }
        return index(request, user)
    }

    @POST
    @Path("/addApi")
    public fun addApi(@Context request: HttpServletRequest, @Restricted(Authority.ROLE_ADMIN) user: User,
                      @FormParam("name") name: String, @FormParam("baseUrl") baseUrl: String,
                      @FormParam("downloadUrl") downloadUrl: String): View {
        apiDao.save(ApiEvent(user.email, name, baseUrl, downloadUrl))
        return index(request, user)
    }

    @GET
    @Path("/deleteApi/{id}")
    public fun deleteApi(@Context request: HttpServletRequest,
                         @Restricted(Authority.ROLE_ADMIN) user: User,
                         @PathParam("id") id: String): View {
        apiDao.save(ApiEvent(EventType.DELETE, user.email, ObjectId(id)))
        return index(request, user)
    }

    @POST
    @Path("/saveChannel")
    public fun saveChannel(@Context request: HttpServletRequest, @Restricted(Authority.ROLE_ADMIN) user: User,
                           @FormParam("id") id: String?, @FormParam("name") name: String, @FormParam("key") key: String,
                           @FormParam("logged") logged: Boolean): View {
        val channel = if (id == null) Channel(name, key, logged) else Channel(ObjectId(id), name, key, logged)
        channelDao.save(channel)
        return index(request, user)
    }

}
