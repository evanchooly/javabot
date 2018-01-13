package javabot.web.resources

import com.antwerkz.sofia.Sofia
import com.codahale.metrics.annotation.Timed
import com.google.common.base.Optional
import javabot.dao.AdminDao
import javabot.model.Admin
import javabot.web.JavabotConfiguration
import javabot.web.model.Authority.ROLE_ADMIN
import javabot.web.model.Authority.ROLE_PUBLIC
import javabot.web.model.InMemoryUserCache.INSTANCE
import javabot.web.model.User
import org.brickred.socialauth.SocialAuthConfig
import org.brickred.socialauth.SocialAuthManager
import org.brickred.socialauth.util.SocialAuthUtil
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.URISyntaxException
import java.util.UUID
import javax.inject.Inject
import javax.servlet.http.HttpServletRequest
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.NewCookie
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.Status.BAD_REQUEST
import javax.ws.rs.core.Response.Status.UNAUTHORIZED

@Path("/auth")
@Produces(MediaType.TEXT_HTML)
class PublicOAuthResource @Inject constructor(var adminDao: AdminDao) {

    var configuration: JavabotConfiguration? = null

    @GET
    @Path("/login")
    @Throws(URISyntaxException::class)
    fun requestOAuth(@Context request: HttpServletRequest): Response {
        val oauthCfg = configuration!!.OAuthCfg
        if (oauthCfg != null) {
            try {
                val manager = getSocialAuthManager()

                request.session.setAttribute(AUTH_MANAGER, manager)

                val uri = URI(manager?.getAuthenticationUrl("googleplus", configuration!!.OAuthSuccessUrl))
                return Response.temporaryRedirect(uri).build()
            } catch (e: Exception) {
                log.error(e.message, e)
            }

        }
        throw WebApplicationException(BAD_REQUEST)
    }

    /**
     * Handles the OAuth server response to the earlier AuthRequest

     * @return The OAuth identifier for this user if verification was successful
     */
    @GET
    @Timed
    @Path("/verify")
    fun verifyOAuthServerResponse(@Context request: HttpServletRequest): Response {
        val manager = request.session.getAttribute(AUTH_MANAGER) as SocialAuthManager

        try {
            val params = SocialAuthUtil.getRequestParametersMap(request)
            val provider = manager.connect(params)

            val p = provider.userProfile

            Sofia.loggingInUser(p)

            var tempUser = User(UUID.randomUUID(), p.email, p.validatedId, provider.accessGrant)
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

            return Response.temporaryRedirect(URI("/")).cookie(replaceSessionTokenCookie(Optional.of(tempUser))).build()
        } catch (e: Exception) {
            e.printStackTrace()
            log.error(e.message, e)
        }

        // Must have failed to be here
        throw WebApplicationException(UNAUTHORIZED)
    }

    /**
     * @return Get an initialized SocialAuthManager
     */
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

    protected fun replaceSessionTokenCookie(user: Optional<User>): NewCookie {
        if (user.isPresent) {
            val value = user.get().sessionToken.toString()
            log.debug("Replacing session token with {}", value)
            return NewCookie(JavabotConfiguration.SESSION_TOKEN_NAME, value, "/", null, null, 86400 * 30, false)
        } else {
            // Remove the session token cookie
            log.debug("Removing session token")
            return NewCookie(JavabotConfiguration.SESSION_TOKEN_NAME, null, null, null, null, 0, false)
        }
    }

    companion object {

        private val log = LoggerFactory.getLogger(PublicOAuthResource::class.java)
        val AUTH_MANAGER: String = "authManager"
    }
}