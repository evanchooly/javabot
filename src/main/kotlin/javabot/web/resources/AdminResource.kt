package javabot.web.resources

import io.quarkus.qute.TemplateInstance
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.servlet.http.HttpServletRequest
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.FormParam
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import javabot.Javabot
import javabot.JavabotConfig
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javabot.dao.ChannelDao
import javabot.dao.ConfigDao
import javabot.model.Admin
import javabot.model.ApiEvent
import javabot.model.Channel
import javabot.model.javadoc.JavadocApi
import javabot.web.auth.Restricted
import javabot.web.model.Authority
import javabot.web.model.User
import javabot.web.views.TemplateService
import org.bson.types.ObjectId

@Path("/admin")
@ApplicationScoped
class AdminResource
@Inject
constructor(
    var templateService: TemplateService,
    var adminDao: AdminDao,
    var apiDao: ApiDao,
    var configDao: ConfigDao,
    var channelDao: ChannelDao,
    var javabot: Javabot,
    var config: JavabotConfig,
) {

    @GET
    fun index(
        @Context request: HttpServletRequest,
        @Restricted(Authority.ROLE_ADMIN) user: User,
    ): TemplateInstance {
        val current = adminDao.getAdminByEmailAddress(user.email)
        return if (current == null) templateService.createError403View()
        else templateService.createAdminIndexView(request, current, Admin())
    }

    @GET
    @Path("/config")
    fun config(
        @Context request: HttpServletRequest,
        @Restricted(Authority.ROLE_ADMIN) user: User,
    ): TemplateInstance {
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        return templateService.createConfigurationView(request)
    }

    @GET
    @Path("/javadoc")
    fun javadoc(
        @Context request: HttpServletRequest,
        @Restricted(Authority.ROLE_ADMIN) user: User,
    ): TemplateInstance {
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        return templateService.createJavadocAdminView(request)
    }

    @GET
    @Path("/newChannel")
    fun newChannel(
        @Context request: HttpServletRequest,
        @Restricted(Authority.ROLE_ADMIN) user: User,
    ): TemplateInstance {
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        return templateService.createChannelEditView(request, Channel())
    }

    @GET
    @Path("/editChannel/{channel}")
    fun editChannel(
        @Context request: HttpServletRequest,
        @Restricted(Authority.ROLE_ADMIN) user: User,
        @PathParam("channel") channel: String,
    ): TemplateInstance {

        // TODO redirect to / if channel is null
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        return templateService.createChannelEditView(request, channelDao.get(channel)!!)
    }

    @POST
    @Path("/saveChannel")
    fun saveChannel(
        @Context request: HttpServletRequest,
        @Restricted(Authority.ROLE_ADMIN) user: User,
        @FormParam("id") id: String?,
        @FormParam("name") name: String,
        @FormParam("key") key: String,
        @FormParam("logged") logged: Boolean,
    ): TemplateInstance {
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        val channel =
            if (id == null) Channel(name, key, logged) else Channel(ObjectId(id), name, key, logged)
        channelDao.save(channel)
        return index(request, user)
    }

    @POST
    @Path("/saveConfig")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    fun saveConfig(
        @Context request: HttpServletRequest,
        @Restricted(Authority.ROLE_ADMIN) user: User,
        @FormParam("server") server: String,
        @FormParam("url") url: String,
        @FormParam("port") port: Int,
        @FormParam("historyLength") historyLength: Int,
        @FormParam("trigger") trigger: String,
        @FormParam("nick") nick: String,
        @FormParam("password") password: String,
        @FormParam("throttleThreshold") throttleThreshold: Int,
        @FormParam("minimumNickServAge") minimumNickServAge: Int,
    ): TemplateInstance {
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
        return templateService.createConfigurationView(request)
    }

    @GET
    @Path("/enableOperation/{name}")
    fun enableOperation(
        @Context request: HttpServletRequest,
        @Restricted(Authority.ROLE_ADMIN) user: User,
        @PathParam("name") name: String,
    ): TemplateInstance {
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        javabot.enableOperation(name)
        return templateService.createConfigurationView(request)
    }

    @GET
    @Path("/disableOperation/{name}")
    fun disableOperation(
        @Context request: HttpServletRequest,
        @Restricted(Authority.ROLE_ADMIN) user: User,
        @PathParam("name") name: String,
    ): TemplateInstance {
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        javabot.disableOperation(name)
        return templateService.createConfigurationView(request)
    }

    @GET
    @Path("/edit/{id}")
    fun editAdmin(
        @Context request: HttpServletRequest,
        @Restricted(Authority.ROLE_ADMIN) user: User,
        @PathParam("id") id: String,
    ): TemplateInstance {
        val current =
            adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)

        return templateService.createAdminIndexView(request, current, adminDao.find(ObjectId(id)))
    }

    @GET
    @Path("/delete/{id}")
    fun deleteAdmin(
        @Context request: HttpServletRequest,
        @Restricted(Authority.ROLE_ADMIN) user: User,
        @PathParam("id") id: String,
    ): TemplateInstance {
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        val admin = adminDao.find(ObjectId(id))
        if (admin != null && (!admin.botOwner)) {
            adminDao.delete(admin)
        }
        return index(request, user)
    }

    @POST
    @Path("/add")
    fun addAdmin(
        @Context request: HttpServletRequest,
        @Restricted(Authority.ROLE_ADMIN) user: User,
        @FormParam("ircName") ircName: String,
        @FormParam("hostName") hostName: String,
        @FormParam("emailAddress") emailAddress: String,
    ): TemplateInstance {
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
    fun addApi(
        @Context request: HttpServletRequest,
        @Restricted(Authority.ROLE_ADMIN) user: User,
        @FormParam("name") name: String?,
        @FormParam("groupId") groupId: String?,
        @FormParam("artifactId") artifactId: String?,
        @FormParam("version") version: String?,
    ): TemplateInstance {

        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        version?.let {
            val apiName = name ?: artifactId ?: throw WebApplicationException(400)
            val api = JavadocApi(config, apiName, groupId ?: "", artifactId ?: "", version)
            apiDao.save(api)
            apiDao.save(ApiEvent.add(user.email, api))
        }

        return javadoc(request, user)
    }

    @GET
    @Path("/deleteApi/{id}")
    fun deleteApi(
        @Context request: HttpServletRequest,
        @Restricted(Authority.ROLE_ADMIN) user: User,
        @PathParam("id") id: String,
    ): TemplateInstance {
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        apiDao.delete(ObjectId(id))
        return javadoc(request, user)
    }

    @GET
    @Path("/reloadApi/{id}")
    fun reloadApi(
        @Context request: HttpServletRequest,
        @Restricted(Authority.ROLE_ADMIN) user: User,
        @PathParam("id") id: String,
    ): TemplateInstance {
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        apiDao.find(ObjectId(id))?.let { apiDao.save(ApiEvent.reload(user.email, it)) }
        return javadoc(request, user)
    }
}
