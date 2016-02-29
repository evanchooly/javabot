package javabot.web.auth

import com.sun.jersey.api.core.HttpContext
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable
import javabot.web.JavabotConfiguration
import javabot.web.JavabotConfiguration.Companion
import javabot.web.model.Authority
import javabot.web.model.InMemoryUserCache
import javabot.web.model.InMemoryUserCache.INSTANCE
import javabot.web.model.User
import javabot.web.resources.AdminResource
import org.slf4j.LoggerFactory
import java.util.UUID
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response.Status
import javax.ws.rs.core.Response.Status.FORBIDDEN
import javax.ws.rs.core.Response.Status.UNAUTHORIZED

/**
 *
 * Injectable to provide the following to [RestrictedProvider]:   * Performs decode from HTTP request  * Carries
 * OpenID authentication data
 * @param requiredAuthorities The required authorities as provided by the RestrictedTo annotation
 * @since 0.0.1
 */
class OpenIDRestrictedToInjectable (requiredAuthorities: Array<out Authority>) : AbstractHttpContextInjectable<User>() {

    private val requiredAuthorities: Set<Authority>

    init {
        this.requiredAuthorities = requiredAuthorities.toSet()
    }

    override fun getValue(httpContext: HttpContext): User {

        try {
            val cookieMap = httpContext.request.cookies
            if (!cookieMap.containsKey(JavabotConfiguration.SESSION_TOKEN_NAME)) {
                throw WebApplicationException(UNAUTHORIZED)
            }

            val sessionToken = UUID.fromString(cookieMap[JavabotConfiguration.SESSION_TOKEN_NAME]?.value)
            val credentials = OpenIDCredentials(sessionToken, requiredAuthorities)

            val user = INSTANCE.getBySessionToken(credentials.sessionToken.toString())
                    ?: throw WebApplicationException(UNAUTHORIZED)
            if (!user.hasAllAuthorities(credentials.requiredAuthorities)) {
                throw WebApplicationException(FORBIDDEN)
            }
            return user
        } catch (e: IllegalArgumentException) {
            log.debug("Error decoding credentials", e)
        }

        // Must have failed to be here
        throw WebApplicationException(UNAUTHORIZED)
    }

    companion object {

        private val log = LoggerFactory.getLogger(OpenIDRestrictedToInjectable::class.java)
    }

}