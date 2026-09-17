package javabot.web.resources

import com.google.common.base.Optional
import com.google.inject.Injector
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.servlet.http.HttpServletRequest
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.NewCookie
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.Response.Status.BAD_REQUEST
import jakarta.ws.rs.core.Response.Status.UNAUTHORIZED
import java.net.URI
import java.net.URISyntaxException
import javabot.dao.AdminDao
import javabot.model.Admin
import javabot.web.JavabotConfiguration
import javabot.web.model.Authority.ROLE_ADMIN
import javabot.web.model.Authority.ROLE_PUBLIC
import javabot.web.model.InMemoryUserCache.INSTANCE
import javabot.web.model.User
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.slf4j.LoggerFactory

@Path("/auth")
@Produces(MediaType.TEXT_HTML)
@ApplicationScoped
class PublicOAuthResource @Inject constructor(private val injector: Injector) {

    // AdminDao is Guice-managed, not a CDI bean -- see GuiceInjectorProducer.
    private val adminDao: AdminDao by lazy { injector.getInstance(AdminDao::class.java) }

    @ConfigProperty(name = "javabot.oauth.success.url", defaultValue = "/")
    lateinit var oauthSuccessUrl: String

    // Optional<String> rather than a nullable String: MicroProfile Config treats an unset
    // property on a plain String field as required even with an empty defaultValue.
    @ConfigProperty(name = "javabot.oauth.config")
    lateinit var oauthConfigPath: java.util.Optional<String>

    @GET
    @Path("/login")
    @Throws(URISyntaxException::class)
    fun requestOAuth(@Context request: HttpServletRequest): Response {
        if (oauthConfigPath.isPresent && oauthConfigPath.get().isNotEmpty()) {
            // If the user already has a session cookie, they're considered authenticated.
            val user =
                INSTANCE.getBySessionToken(
                    request.cookies
                        ?.firstOrNull { it.name == JavabotConfiguration.SESSION_TOKEN_NAME }
                        ?.value
                )
            if (user != null) {
                user.authorities.add(ROLE_PUBLIC)
                val admin = adminDao.getAdminByEmailAddress(user.email)
                if (admin != null) {
                    user.authorities.add(ROLE_ADMIN)
                }
                INSTANCE.put(user)
                return Response.temporaryRedirect(URI(oauthSuccessUrl))
                    .cookie(replaceSessionTokenCookie(Optional.of(user)))
                    .build()
            }
        }
        throw WebApplicationException(BAD_REQUEST)
    }

    /**
     * Handles the OAuth server response.
     *
     * @return The OAuth identifier for this user if verification was successful
     */
    @GET
    @Path("/verify")
    fun verifyOAuthServerResponse(@Context request: HttpServletRequest): Response {
        try {
            val user =
                INSTANCE.getBySessionToken(
                    request.cookies
                        ?.firstOrNull { it.name == JavabotConfiguration.SESSION_TOKEN_NAME }
                        ?.value
                )
            if (user != null) {
                user.authorities.add(ROLE_PUBLIC)
                val admin = adminDao.getAdminByEmailAddress(user.email)
                if (admin != null) {
                    user.authorities.add(ROLE_ADMIN)
                } else {
                    if (adminDao.count() == 0L) {
                        adminDao.save(Admin(user.email))
                        user.authorities.add(ROLE_ADMIN)
                    }
                }
                INSTANCE.put(user)
                return Response.temporaryRedirect(URI("/"))
                    .cookie(replaceSessionTokenCookie(Optional.of(user)))
                    .build()
            }
            throw WebApplicationException(UNAUTHORIZED)
        } catch (e: Exception) {
            log.error("OAuth verification failed: {}", e.message, e)
            throw WebApplicationException(UNAUTHORIZED)
        }
    }

    /** @return Get an initialized User from session cookie */
    protected fun replaceSessionTokenCookie(user: Optional<User>): NewCookie {
        if (user.isPresent) {
            val value = user.get().sessionToken.toString()
            log.debug("Replacing session token with {}", value)
            return NewCookie(
                JavabotConfiguration.SESSION_TOKEN_NAME,
                value,
                "/",
                null,
                null,
                86400 * 30,
                false,
            )
        } else {
            // Remove the session token cookie
            log.debug("Removing session token")
            return NewCookie(
                JavabotConfiguration.SESSION_TOKEN_NAME,
                null,
                null,
                null,
                null,
                0,
                false,
            )
        }
    }

    companion object {

        private val log = LoggerFactory.getLogger(PublicOAuthResource::class.java)
        val AUTH_MANAGER: String = "authManager"
    }
}
