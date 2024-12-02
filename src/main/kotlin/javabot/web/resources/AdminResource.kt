package javabot.web.resources

import io.quarkus.qute.CheckedTemplate
import io.quarkus.qute.TemplateInstance
import jakarta.inject.Inject
import jakarta.servlet.http.HttpServletRequest
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.FormParam
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
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
import javabot.web.views.ViewFactory
import org.bson.types.ObjectId

@Path("/admin")
@CheckedTemplate
class AdminResource
@Inject
constructor(
    val viewFactory: ViewFactory,
    val adminDao: AdminDao,
    val apiDao: ApiDao,
    val configDao: ConfigDao,
    val channelDao: ChannelDao,
    val javabot: Javabot,
    val config: JavabotConfig,
) {
    companion object {
        @JvmStatic external fun index(): TemplateInstance

        @JvmStatic external fun config(): TemplateInstance

        @JvmStatic external fun javadoc(): TemplateInstance

        @JvmStatic external fun newChannel(): TemplateInstance

        @JvmStatic external fun editChannel(): TemplateInstance

        @JvmStatic external fun enableOperation(): TemplateInstance

        @JvmStatic external fun disableOperation(): TemplateInstance

        @JvmStatic external fun editAdmin(): TemplateInstance

        @JvmStatic external fun deleteAdmin(): TemplateInstance

        @JvmStatic external fun addAdmin(): TemplateInstance

        @JvmStatic external fun addApi(): TemplateInstance

        @JvmStatic external fun deleteApi(): TemplateInstance

        @JvmStatic external fun reloadApi(): TemplateInstance
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    fun index(
        @Context request: HttpServletRequest,
        @Restricted(Authority.ROLE_ADMIN) user: User,
    ) =
        adminDao.getAdminByEmailAddress(user.email)?.let { index() }
            ?: PublicErrorResource.view403()

    @GET
    @Path("/config")
    fun config(
        @Context request: HttpServletRequest,
        @Restricted(Authority.ROLE_ADMIN) user: User,
    ) =
        adminDao.getAdminByEmailAddress(user.email)?.let { config() }
            ?: PublicErrorResource.view403()

    @GET
    @Path("/javadoc")
    fun javadoc(
        @Context request: HttpServletRequest,
        @Restricted(Authority.ROLE_ADMIN) user: User,
    ) =
        adminDao.getAdminByEmailAddress(user.email)?.let { javadoc() }
            ?: PublicErrorResource.view403()

    @GET
    @Path("/newChannel")
    fun newChannel(
        @Context request: HttpServletRequest,
        @Restricted(Authority.ROLE_ADMIN) user: User,
    ) =
        adminDao.getAdminByEmailAddress(user.email)?.let { newChannel() }
            ?: PublicErrorResource.view403()

    @GET
    @Path("/editChannel/{channel}")
    fun editChannel(
        @Context request: HttpServletRequest,
        @Restricted(Authority.ROLE_ADMIN) user: User,
        @PathParam("channel") channel: String,
    ) =
        adminDao.getAdminByEmailAddress(user.email)?.let { editChannel() }
            ?: PublicErrorResource.view403()

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
        return config()
    }

    @GET
    @Path("/enableOperation/{name}")
    fun enableOperation(
        @Context request: HttpServletRequest,
        @Restricted(Authority.ROLE_ADMIN) user: User,
        @PathParam("name") name: String,
    ) =
        adminDao.getAdminByEmailAddress(user.email)?.let {
            javabot.enableOperation(name)

            config()
        }
            ?: PublicErrorResource.view403()

    @GET
    @Path("/disableOperation/{name}")
    fun disableOperation(
        @Context request: HttpServletRequest,
        @Restricted(Authority.ROLE_ADMIN) user: User,
        @PathParam("name") name: String,
    ) =
        adminDao.getAdminByEmailAddress(user.email)?.let {
            javabot.disableOperation(name)
            config()
        }
            ?: PublicErrorResource.view403()

    @GET
    @Path("/edit/{id}")
    fun editAdmin(
        @Context request: HttpServletRequest,
        @Restricted(Authority.ROLE_ADMIN) user: User,
        @PathParam("id") id: String,
    ) =
        adminDao.getAdminByEmailAddress(user.email)?.let { editAdmin() }
            ?: PublicErrorResource.view403()

    @GET
    @Path("/delete/{id}")
    fun deleteAdmin(
        @Context request: HttpServletRequest,
        @Restricted(Authority.ROLE_ADMIN) user: User,
        @PathParam("id") id: String,
    ) =
        adminDao.getAdminByEmailAddress(user.email)?.let {
            val admin = adminDao.find(ObjectId(id))
            if (admin != null && (!admin.botOwner)) {
                adminDao.delete(admin)
            }
            index()
        }
            ?: PublicErrorResource.view403()

    @POST
    @Path("/add")
    fun addAdmin(
        @Context request: HttpServletRequest,
        @Restricted(Authority.ROLE_ADMIN) user: User,
        @FormParam("ircName") ircName: String,
        @FormParam("hostName") hostName: String,
        @FormParam("emailAddress") emailAddress: String,
    ): TemplateInstance {
        var admin =
            adminDao.getAdminByEmailAddress(emailAddress)?.also {
                it.ircName = ircName
                it.hostName = hostName
                it.emailAddress = emailAddress
                it
            }
                ?: Admin(ircName, emailAddress, hostName, true)

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
