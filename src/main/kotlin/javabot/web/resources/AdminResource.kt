package javabot.web.resources

import io.quarkus.qute.TemplateInstance
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.FormParam
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.WebApplicationException
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
import javabot.web.model.Authority
import javabot.web.model.InMemoryUserCache.INSTANCE
import javabot.web.model.User
import javabot.web.views.TemplateService
import org.bson.types.ObjectId
import org.eclipse.microprofile.jwt.Claims
import org.eclipse.microprofile.jwt.JsonWebToken

@Path("/admin")
@ApplicationScoped
class AdminResource
@Inject
constructor(
    var templateService: TemplateService,
    private val adminDao: AdminDao,
    private val apiDao: ApiDao,
    private val configDao: ConfigDao,
    private val channelDao: ChannelDao,
    private val javabot: Javabot,
    private val config: JavabotConfig,
) {

    // Populated by quarkus-oidc for web-app applications: the ID token of the session Quarkus
    // established during its own OIDC redirect/callback flow. Request-scoped bean, so it is
    // injected once here and re-resolved per request through its client proxy.
    @Inject lateinit var idToken: JsonWebToken

    /**
     * The admin behind the current request.
     *
     * Authentication itself is enforced *before* any method here runs, by
     * `quarkus.http.auth.permission.admin.*` in application.properties -- reaching a resource
     * method at all already means "authenticated". What remains is the authorization check, and
     * this method is the single choke point for it: it fails closed with 403 unless the
     * authenticated identity's email is a registered [Admin]. (The previous cookie-based
     * `currentUser` enforced `ROLE_ADMIN` the same way, via `OpenIDCredentials`, so every endpoint
     * -- including ones with no additional check of their own, such as [addAdmin] -- keeps exactly
     * the authorization it had before.)
     */
    private fun currentUser(): User {
        val email =
            idToken.getClaim<String>(Claims.email.name) ?: throw WebApplicationException(403)
        // An unverified `email` claim is self-declared by the end user at some providers, so it
        // must not drive an authorization decision. Fail closed when the companion
        // `email_verified` claim is absent as well as when it is false: a provider that never
        // asserts verification cannot be taken at its word either. Compared as a string because
        // the claim arrives as a Boolean, a JsonValue, or a string depending on the provider and
        // the OIDC principal implementation; only a literal true passes.
        if (idToken.getClaim<Any?>(Claims.email_verified.name)?.toString() != "true") {
            throw WebApplicationException(403)
        }
        adminDao.getAdminByEmailAddress(email) ?: throw WebApplicationException(403)

        // A fresh random token per request, deliberately *not* derived from the email.
        // InMemoryUserCache is shared with BotResource's public-page cookie lookup, so any key
        // computable from a known admin email address would let an anonymous visitor forge that
        // key as their own session cookie and be served as that admin. currentUser() re-verifies
        // identity from idToken (Quarkus OIDC's own signed session) on every request, so this
        // cache entry never needs to outlive the current request -- it exists only so the
        // TemplateService.isLoggedIn/isAdmin lookups later in this same render can find it, and
        // then expires unreferenced.
        val user = User(UUID.randomUUID(), email, idToken.subject ?: email)
        user.authorities.add(Authority.ROLE_PUBLIC)
        user.authorities.add(Authority.ROLE_ADMIN)
        INSTANCE.put(user)
        return user
    }

    private fun sessionToken(user: User): String? = user.sessionToken?.toString()

    @GET
    fun index(): TemplateInstance {
        val user = currentUser()
        val current = adminDao.getAdminByEmailAddress(user.email)
        return if (current == null) templateService.createError403View()
        else templateService.createAdminIndexView(sessionToken(user), current, Admin())
    }

    @GET
    @Path("/config")
    fun config(): TemplateInstance {
        val user = currentUser()
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        return templateService.createConfigurationView(sessionToken(user))
    }

    @GET
    @Path("/javadoc")
    fun javadoc(): TemplateInstance {
        val user = currentUser()
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        return templateService.createJavadocAdminView(sessionToken(user))
    }

    @GET
    @Path("/newChannel")
    fun newChannel(): TemplateInstance {
        val user = currentUser()
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        return templateService.createChannelEditView(sessionToken(user), Channel())
    }

    @GET
    @Path("/editChannel/{channel}")
    fun editChannel(@PathParam("channel") channel: String): TemplateInstance {
        val user = currentUser()
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)

        val channelOpt = channelDao.get(channel)
        if (channelOpt == null) {
            return templateService.createIndexView(sessionToken(user))
        }
        return templateService.createChannelEditView(sessionToken(user), channelOpt)
    }

    @POST
    @Path("/saveChannel")
    fun saveChannel(
        @FormParam("id") id: String?,
        @FormParam("name") name: String,
        @FormParam("key") key: String,
        @FormParam("logged") logged: Boolean,
    ): TemplateInstance {
        val user = currentUser()
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        val channel =
            if (id == null) Channel(name, key, logged) else Channel(ObjectId(id), name, key, logged)
        channelDao.save(channel)
        return index()
    }

    @POST
    @Path("/saveConfig")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    fun saveConfig(
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
        val user = currentUser()
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
        return templateService.createConfigurationView(sessionToken(user))
    }

    @GET
    @Path("/enableOperation/{name}")
    fun enableOperation(@PathParam("name") name: String): TemplateInstance {
        val user = currentUser()
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        javabot.enableOperation(name)
        return templateService.createConfigurationView(sessionToken(user))
    }

    @GET
    @Path("/disableOperation/{name}")
    fun disableOperation(@PathParam("name") name: String): TemplateInstance {
        val user = currentUser()
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        javabot.disableOperation(name)
        return templateService.createConfigurationView(sessionToken(user))
    }

    @GET
    @Path("/edit/{id}")
    fun editAdmin(@PathParam("id") id: String): TemplateInstance {
        val user = currentUser()
        val current =
            adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)

        return templateService.createAdminIndexView(
            sessionToken(user),
            current,
            adminDao.find(ObjectId(id)),
        )
    }

    @GET
    @Path("/delete/{id}")
    fun deleteAdmin(@PathParam("id") id: String): TemplateInstance {
        val user = currentUser()
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        val admin = adminDao.find(ObjectId(id))
        if (admin != null && (!admin.botOwner)) {
            adminDao.delete(admin)
        }
        return index()
    }

    @POST
    @Path("/add")
    fun addAdmin(
        @FormParam("ircName") ircName: String,
        @FormParam("hostName") hostName: String,
        @FormParam("emailAddress") emailAddress: String,
    ): TemplateInstance {
        currentUser()
        var admin: Admin? = adminDao.getAdminByEmailAddress(emailAddress)
        if (admin == null) {
            admin = Admin(ircName, emailAddress, hostName, true)
        } else {
            admin.ircName = ircName
            admin.hostName = hostName
            admin.emailAddress = emailAddress
        }
        adminDao.save(admin)
        return index()
    }

    @POST
    @Path("/addApi")
    fun addApi(
        @FormParam("name") name: String?,
        @FormParam("groupId") groupId: String?,
        @FormParam("artifactId") artifactId: String?,
        @FormParam("version") version: String?,
    ): TemplateInstance {
        val user = currentUser()
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        version?.let {
            val apiName = name ?: artifactId ?: throw WebApplicationException(400)
            val api = JavadocApi(config, apiName, groupId ?: "", artifactId ?: "", version)
            apiDao.save(api)
            javabot.submitEvent(ApiEvent.add(user.email, api))
        }

        return javadoc()
    }

    @GET
    @Path("/deleteApi/{id}")
    fun deleteApi(@PathParam("id") id: String): TemplateInstance {
        val user = currentUser()
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        apiDao.delete(ObjectId(id))
        return javadoc()
    }

    @GET
    @Path("/reloadApi/{id}")
    fun reloadApi(@PathParam("id") id: String): TemplateInstance {
        val user = currentUser()
        adminDao.getAdminByEmailAddress(user.email) ?: throw WebApplicationException(403)
        apiDao.find(ObjectId(id))?.let { javabot.submitEvent(ApiEvent.reload(user.email, it)) }
        return javadoc()
    }
}
