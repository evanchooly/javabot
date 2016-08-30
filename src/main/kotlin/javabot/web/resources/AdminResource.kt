package javabot.web.resources

import io.dropwizard.views.View
import javabot.Javabot
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javabot.dao.ChannelDao
import javabot.dao.ConfigDao
import javabot.model.Admin
import javabot.model.ApiEvent
import javabot.model.Channel
import javabot.model.EventType
import javabot.web.auth.Restricted
import javabot.web.model.Authority
import javabot.web.model.User
import javabot.web.views.ViewFactory
import org.bson.types.ObjectId
import org.w3c.dom.Element
import org.xml.sax.InputSource
import java.io.StringReader
import javax.inject.Inject
import javax.servlet.http.HttpServletRequest
import javax.ws.rs.Consumes
import javax.ws.rs.FormParam
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.xml.parsers.DocumentBuilderFactory

@Path("/admin") class AdminResource
@Inject
constructor(var viewFactory: ViewFactory, var adminDao: AdminDao, var apiDao: ApiDao, var configDao: ConfigDao,
            var channelDao: ChannelDao, var javabot: Javabot) {

    @GET
    fun index(@Context request: HttpServletRequest, @Restricted(Authority.ROLE_ADMIN) user: User): View {
        val current = adminDao.getAdminByEmailAddress(user.email)
        return if (current == null) PublicErrorResource.view403() else viewFactory.createAdminIndexView(request, current, Admin())
    }

    @GET
    @Path("/config")
    fun config(@Context request: HttpServletRequest, @Restricted(Authority.ROLE_ADMIN) user: User): View {
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        return viewFactory.createConfigurationView(request)
    }

    @GET
    @Path("/javadoc")
    fun javadoc(@Context request: HttpServletRequest, @Restricted(Authority.ROLE_ADMIN) user: User): View {
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        return viewFactory.createJavadocAdminView(request)
    }

    @GET
    @Path("/newChannel")
    fun newChannel(@Context request: HttpServletRequest,
                   @Restricted(Authority.ROLE_ADMIN) user: User): View {
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        return viewFactory.createChannelEditView(request, Channel())
    }

    @GET
    @Path("/editChannel/{channel}")
    fun editChannel(@Context request: HttpServletRequest, @Restricted(Authority.ROLE_ADMIN) user: User,
                                                    @PathParam("channel") channel: String): View {

        // TODO redirect to / if channel is null
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        return viewFactory.createChannelEditView(request, channelDao.get(channel)!!)
    }

    @POST
    @Path("/saveChannel")
    fun saveChannel(@Context request: HttpServletRequest, @Restricted(Authority.ROLE_ADMIN) user: User,
                                          @FormParam("id") id: String?, @FormParam("name") name: String, @FormParam("key") key: String,
                                          @FormParam("logged") logged: Boolean): View {
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        val channel = if (id == null) Channel(name, key, logged) else Channel(ObjectId(id), name, key, logged)
        channelDao.save(channel)
        return index(request, user)
    }

    @POST
    @Path("/saveConfig")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    fun saveConfig(@Context request: HttpServletRequest, @Restricted(Authority.ROLE_ADMIN) user: User,
                                                                    @FormParam("server") server: String, @FormParam("url") url: String, @FormParam("port") port: Int,
                                                                    @FormParam("historyLength") historyLength: Int, @FormParam("trigger") trigger: String,
                                                                    @FormParam("nick") nick: String, @FormParam("password") password: String,
                                                                    @FormParam("throttleThreshold") throttleThreshold: Int,
                                                                    @FormParam("minimumNickServAge") minimumNickServAge: Int): View {
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
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
        return viewFactory.createConfigurationView(request)
    }

    @GET
    @Path("/enableOperation/{name}") fun enableOperation(@Context request: HttpServletRequest, @Restricted(Authority.ROLE_ADMIN) user: User,
                                                         @PathParam("name") name: String): View {
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        javabot.enableOperation(name)
        return viewFactory.createConfigurationView(request)
    }

    @GET
    @Path("/disableOperation/{name}") fun disableOperation(@Context request: HttpServletRequest, @Restricted(Authority.ROLE_ADMIN) user: User,
                                                           @PathParam("name") name: String): View {
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        javabot.disableOperation(name)
        return viewFactory.createConfigurationView(request)
    }

    @GET
    @Path("/edit/{id}") fun editAdmin(@Context request: HttpServletRequest, @Restricted(Authority.ROLE_ADMIN) user: User,
                                      @PathParam("id") id: String): View {
        val current = adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)

        return viewFactory.createAdminIndexView(request, current, adminDao.find(ObjectId(id)))
    }

    @GET
    @Path("/delete/{id}") fun deleteAdmin(@Context request: HttpServletRequest, @Restricted(Authority.ROLE_ADMIN) user: User,
                                          @PathParam("id") id: String): View {
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        val admin = adminDao.find(ObjectId(id))
        if (admin != null && (!admin.botOwner)) {
            adminDao.delete(admin)
        }
        return index(request, user)
    }

    @POST
    @Path("/add")
    fun addAdmin(@Context request: HttpServletRequest, @Restricted(Authority.ROLE_ADMIN) user: User,
                 @FormParam("ircName") ircName: String, @FormParam("hostName") hostName: String,
                 @FormParam("emailAddress") emailAddress: String): View {
        var admin: Admin? = adminDao.getAdminByEmailAddress(emailAddress)
        if (admin == null) {
            admin = Admin(ircName, emailAddress, hostName, true)
        } else {
            admin.ircName = ircName
            admin.hostName = hostName
            admin.emailAddress = emailAddress
        }
        adminDao.save(admin)
        return index(request, user)
    }

    @POST
    @Path("/addApi")
    fun addApi(@Context request: HttpServletRequest, @Restricted(Authority.ROLE_ADMIN) user: User,
               @FormParam("name") name: String, @FormParam("dependency") depString: String): View {

        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        val event = if (depString != "") {
            val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(InputSource(StringReader(depString)))

            val dependency = (document.getElementsByTagName("dependency").item(0) as Element)
            val groupId = dependency.getElementsByTagName("groupId").item(0).textContent
            val artifactId = dependency.getElementsByTagName("artifactId").item(0).textContent
            val version = dependency.getElementsByTagName("version").item(0).textContent
            ApiEvent(user.email, if (name != "") name else artifactId, groupId, artifactId, version)
        } else ApiEvent(user.email, name, "", "", "")

        apiDao.save(event)
        return javadoc(request, user)
    }

    @GET
    @Path("/deleteApi/{id}")
    fun deleteApi(@Context request: HttpServletRequest,
                  @Restricted(Authority.ROLE_ADMIN) user: User,
                  @PathParam("id") id: String): View {
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        apiDao.save(ApiEvent(user.email, EventType.DELETE, ObjectId(id)))
        return javadoc(request, user)
    }

    @GET
    @Path("/reloadApi/{id}")
    fun reloadApi(@Context request: HttpServletRequest,
                  @Restricted(Authority.ROLE_ADMIN) user: User,
                  @PathParam("id") id: String): View {
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        apiDao.save(ApiEvent(user.email, EventType.RELOAD, ObjectId(id)))
        return javadoc(request, user)
    }

}