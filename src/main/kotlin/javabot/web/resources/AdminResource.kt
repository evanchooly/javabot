package javabot.web.resources

import com.google.inject.Injector
import io.quarkus.qute.TemplateInstance
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.CookieParam
import jakarta.ws.rs.FormParam
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Cookie
import jakarta.ws.rs.core.MediaType
import java.util.UUID
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
import javabot.web.JavabotConfiguration
import javabot.web.auth.OpenIDAuthenticator
import javabot.web.auth.OpenIDCredentials
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
    private val authenticator: OpenIDAuthenticator,
    private val injector: Injector,
) {

    // Guice-managed, not CDI beans -- see GuiceInjectorProducer.
    private val adminDao: AdminDao by lazy { injector.getInstance(AdminDao::class.java) }
    private val apiDao: ApiDao by lazy { injector.getInstance(ApiDao::class.java) }
    private val configDao: ConfigDao by lazy { injector.getInstance(ConfigDao::class.java) }
    private val channelDao: ChannelDao by lazy { injector.getInstance(ChannelDao::class.java) }
    private val javabot: Javabot by lazy { injector.getInstance(Javabot::class.java) }
    private val config: JavabotConfig by lazy { injector.getInstance(JavabotConfig::class.java) }

    // Every endpoint here requires an admin session. RESTEasy Reactive resolves resource method
    // parameters itself (unlike Dropwizard/Jersey, it has no notion of a custom @Restricted
    // annotation providing one), so the authenticated user is looked up explicitly instead.
    private fun currentUser(sessionCookie: Cookie?): User {
        val cookie = sessionCookie ?: throw WebApplicationException(401)
        val sessionToken =
            try {
                UUID.fromString(cookie.value)
            } catch (_: IllegalArgumentException) {
                throw WebApplicationException(401)
            }
        return authenticator
            .authenticate(OpenIDCredentials(sessionToken, setOf(Authority.ROLE_ADMIN)))
            .orElseThrow { WebApplicationException(401) }
    }

    @GET
    fun index(
        @CookieParam(JavabotConfiguration.SESSION_TOKEN_NAME) sessionCookie: Cookie?
    ): TemplateInstance {
        val user = currentUser(sessionCookie)
        val current = adminDao.getAdminByEmailAddress(user.email)
        return if (current == null) templateService.createError403View()
        else templateService.createAdminIndexView(sessionCookie?.value, current, Admin())
    }

    @GET
    @Path("/config")
    fun config(
        @CookieParam(JavabotConfiguration.SESSION_TOKEN_NAME) sessionCookie: Cookie?
    ): TemplateInstance {
        val user = currentUser(sessionCookie)
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        return templateService.createConfigurationView(sessionCookie?.value)
    }

    @GET
    @Path("/javadoc")
    fun javadoc(
        @CookieParam(JavabotConfiguration.SESSION_TOKEN_NAME) sessionCookie: Cookie?
    ): TemplateInstance {
        val user = currentUser(sessionCookie)
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        return templateService.createJavadocAdminView(sessionCookie?.value)
    }

    @GET
    @Path("/newChannel")
    fun newChannel(
        @CookieParam(JavabotConfiguration.SESSION_TOKEN_NAME) sessionCookie: Cookie?
    ): TemplateInstance {
        val user = currentUser(sessionCookie)
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        return templateService.createChannelEditView(sessionCookie?.value, Channel())
    }

    @GET
    @Path("/editChannel/{channel}")
    fun editChannel(
        @CookieParam(JavabotConfiguration.SESSION_TOKEN_NAME) sessionCookie: Cookie?,
        user: User,
        @PathParam("channel") channel: String,
    ): TemplateInstance {
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)

        val channelOpt = channelDao.get(channel)
        if (channelOpt == null) {
            return templateService.createIndexView(sessionCookie?.value)
        }
        return templateService.createChannelEditView(sessionCookie?.value, channelOpt)
    }

    @POST
    @Path("/saveChannel")
    fun saveChannel(
        @CookieParam(JavabotConfiguration.SESSION_TOKEN_NAME) sessionCookie: Cookie?,
        @FormParam("id") id: String?,
        @FormParam("name") name: String,
        @FormParam("key") key: String,
        @FormParam("logged") logged: Boolean,
    ): TemplateInstance {
        val user = currentUser(sessionCookie)
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        val channel =
            if (id == null) Channel(name, key, logged) else Channel(ObjectId(id), name, key, logged)
        channelDao.save(channel)
        return index(sessionCookie)
    }

    @POST
    @Path("/saveConfig")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    fun saveConfig(
        @CookieParam(JavabotConfiguration.SESSION_TOKEN_NAME) sessionCookie: Cookie?,
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
        val user = currentUser(sessionCookie)
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
        return templateService.createConfigurationView(sessionCookie?.value)
    }

    @GET
    @Path("/enableOperation/{name}")
    fun enableOperation(
        @CookieParam(JavabotConfiguration.SESSION_TOKEN_NAME) sessionCookie: Cookie?,
        @PathParam("name") name: String,
    ): TemplateInstance {
        val user = currentUser(sessionCookie)
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        javabot.enableOperation(name)
        return templateService.createConfigurationView(sessionCookie?.value)
    }

    @GET
    @Path("/disableOperation/{name}")
    fun disableOperation(
        @CookieParam(JavabotConfiguration.SESSION_TOKEN_NAME) sessionCookie: Cookie?,
        @PathParam("name") name: String,
    ): TemplateInstance {
        val user = currentUser(sessionCookie)
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        javabot.disableOperation(name)
        return templateService.createConfigurationView(sessionCookie?.value)
    }

    @GET
    @Path("/edit/{id}")
    fun editAdmin(
        @CookieParam(JavabotConfiguration.SESSION_TOKEN_NAME) sessionCookie: Cookie?,
        @PathParam("id") id: String,
    ): TemplateInstance {
        val user = currentUser(sessionCookie)
        val current =
            adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)

        return templateService.createAdminIndexView(
            sessionCookie?.value,
            current,
            adminDao.find(ObjectId(id)),
        )
    }

    @GET
    @Path("/delete/{id}")
    fun deleteAdmin(
        @CookieParam(JavabotConfiguration.SESSION_TOKEN_NAME) sessionCookie: Cookie?,
        @PathParam("id") id: String,
    ): TemplateInstance {
        val user = currentUser(sessionCookie)
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        val admin = adminDao.find(ObjectId(id))
        if (admin != null && (!admin.botOwner)) {
            adminDao.delete(admin)
        }
        return index(sessionCookie)
    }

    @POST
    @Path("/add")
    fun addAdmin(
        @CookieParam(JavabotConfiguration.SESSION_TOKEN_NAME) sessionCookie: Cookie?,
        @FormParam("ircName") ircName: String,
        @FormParam("hostName") hostName: String,
        @FormParam("emailAddress") emailAddress: String,
    ): TemplateInstance {
        currentUser(sessionCookie)
        var admin: Admin? = adminDao.getAdminByEmailAddress(emailAddress)
        if (admin == null) {
            admin = Admin(ircName, emailAddress, hostName, true)
        } else {
            admin.ircName = ircName
            admin.hostName = hostName
            admin.emailAddress = emailAddress
        }
        adminDao.save(admin)
        return index(sessionCookie)
    }

    @POST
    @Path("/addApi")
    fun addApi(
        @CookieParam(JavabotConfiguration.SESSION_TOKEN_NAME) sessionCookie: Cookie?,
        @FormParam("name") name: String?,
        @FormParam("groupId") groupId: String?,
        @FormParam("artifactId") artifactId: String?,
        @FormParam("version") version: String?,
    ): TemplateInstance {
        val user = currentUser(sessionCookie)
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        version?.let {
            val apiName = name ?: artifactId ?: throw WebApplicationException(400)
            val api = JavadocApi(config, apiName, groupId ?: "", artifactId ?: "", version)
            apiDao.save(api)
            apiDao.save(ApiEvent.add(user.email, api))
        }

        return javadoc(sessionCookie)
    }

    @GET
    @Path("/deleteApi/{id}")
    fun deleteApi(
        @CookieParam(JavabotConfiguration.SESSION_TOKEN_NAME) sessionCookie: Cookie?,
        @PathParam("id") id: String,
    ): TemplateInstance {
        val user = currentUser(sessionCookie)
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        apiDao.delete(ObjectId(id))
        return javadoc(sessionCookie)
    }

    @GET
    @Path("/reloadApi/{id}")
    fun reloadApi(
        @CookieParam(JavabotConfiguration.SESSION_TOKEN_NAME) sessionCookie: Cookie?,
        @PathParam("id") id: String,
    ): TemplateInstance {
        val user = currentUser(sessionCookie)
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        apiDao.find(ObjectId(id))?.let { apiDao.save(ApiEvent.reload(user.email, it)) }
        return javadoc(sessionCookie)
    }
}
