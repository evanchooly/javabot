package javabot.kotlin.web.auth

import com.sun.jersey.api.core.HttpContext
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable
import javabot.kotlin.web.JavabotConfiguration
import javabot.kotlin.web.model.Authority
import javabot.kotlin.web.model.InMemoryUserCache
import javabot.kotlin.web.model.User
import javabot.kotlin.web.resources.AdminResource
import org.slf4j.LoggerFactory
import java.util.UUID
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response.Status

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
                throw WebApplicationException(Status.UNAUTHORIZED)
            }

            val sessionToken = UUID.fromString(cookieMap[JavabotConfiguration.SESSION_TOKEN_NAME]?.value)
            val credentials = OpenIDCredentials(sessionToken, requiredAuthorities)

            val user = InMemoryUserCache.INSTANCE.getBySessionToken(credentials.sessionToken.toString())
                    ?: throw WebApplicationException(Status.UNAUTHORIZED)
            if (!user.hasAllAuthorities(credentials.requiredAuthorities)) {
                throw WebApplicationException(Status.FORBIDDEN)
            }
            return user
        } catch (e: IllegalArgumentException) {
            log.debug("Error decoding credentials", e)
        }

        // Must have failed to be here
        throw WebApplicationException(Status.UNAUTHORIZED)
    }

    companion object {

        private val log = LoggerFactory.getLogger(OpenIDRestrictedToInjectable::class.java)
    }

}