package javabot.web.resources

import com.antwerkz.sofia.Sofia
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import java.net.URI
import java.util.UUID
import javabot.dao.AdminDao
import javabot.model.Admin
import javabot.web.JavabotConfiguration
import javabot.web.UserSession
import javabot.web.model.Authority.ROLE_ADMIN
import javabot.web.model.Authority.ROLE_PUBLIC
import javabot.web.model.InMemoryUserCache.INSTANCE
import javabot.web.model.User
import javax.inject.Inject
import org.brickred.socialauth.SocialAuthConfig
import org.brickred.socialauth.SocialAuthManager
import org.slf4j.LoggerFactory

class PublicOAuthResource @Inject constructor(var adminDao: AdminDao) {

    var configuration: JavabotConfiguration? = null

    fun configureRoutes(routing: Routing) {
        with(routing) {
            get("/auth/login") {
                val oauthCfg = configuration!!.OAuthCfg
                if (oauthCfg != null) {
                    try {
                        val manager = getSocialAuthManager()

                        // Store manager in session
                        call.sessions.set(UserSession(AUTH_MANAGER))

                        val uri =
                            URI(
                                manager?.getAuthenticationUrl(
                                    "googleplus",
                                    configuration!!.OAuthSuccessUrl,
                                )
                            )
                        call.respondRedirect(uri.toString())
                        return@get
                    } catch (e: Exception) {
                        log.error(e.message, e)
                    }
                }
                call.respond(HttpStatusCode.BadRequest)
            }

            get("/auth/verify") {
                // TODO: Retrieve manager from session
                val manager =
                    getSocialAuthManager()
                        ?: run {
                            call.respond(HttpStatusCode.Unauthorized)
                            return@get
                        }

                try {
                    val params = mutableMapOf<String, String>()
                    call.request.queryParameters.entries().forEach { entry ->
                        params[entry.key] = entry.value.firstOrNull() ?: ""
                    }
                    val provider = manager.connect(params)

                    val p = provider.userProfile

                    Sofia.loggingInUser(p)

                    var tempUser =
                        User(UUID.randomUUID(), p.email, p.validatedId, provider.accessGrant)
                    tempUser.authorities.add(ROLE_PUBLIC)

                    val user = INSTANCE.getByOpenIDIdentifier(tempUser.openIDIdentifier)
                    if (user == null) {
                        val admin = adminDao.getAdminByEmailAddress(tempUser.email)
                        if (admin != null) {
                            tempUser.authorities.add(ROLE_ADMIN)
                        } else {
                            if (adminDao.count() == 0L) {
                                adminDao.save(Admin(tempUser.email))
                                tempUser.authorities.add(ROLE_ADMIN)
                            }
                        }
                        INSTANCE.put(tempUser)
                    } else {
                        tempUser = user
                    }

                    call.sessions.set(UserSession(tempUser.sessionToken.toString()))
                    call.respondRedirect("/")
                } catch (e: Exception) {
                    e.printStackTrace()
                    log.error(e.message, e)
                    call.respond(HttpStatusCode.Unauthorized)
                }
            }
        }
    }

    /** @return Get an initialized SocialAuthManager */
    private fun getSocialAuthManager(): SocialAuthManager? {
        val config = SocialAuthConfig.getDefault()
        try {
            config.load(configuration!!.getOAuthCfgProperties())
            val manager = SocialAuthManager()
            manager.socialAuthConfig = config
            return manager
        } catch (e: Exception) {
            log.error(e.message, e)
        }

        return null
    }

    companion object {

        private val log = LoggerFactory.getLogger(PublicOAuthResource::class.java)
        val AUTH_MANAGER: String = "authManager"
    }
}
