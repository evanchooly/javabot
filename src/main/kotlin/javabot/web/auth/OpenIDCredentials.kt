package javabot.web.auth

import java.util.UUID
import javabot.web.model.Authority

/**
 *
 * Value object to provide the following to [OpenIDAuthenticator]:
 *
 * * Storage of the necessary credentials for OpenID authentication
 *
 * @since 0.0.1
 */
class OpenIDCredentials
/**
 * @param sessionToken The session token acting as a surrogate for the OpenID token
 * *
 * @param requiredAuthorities The authorities required to authenticate (provided by the [Restricted]
 * annotation)
 */
(sessionToken: UUID, requiredAuthorities: Set<Authority>) {

    val sessionToken: UUID
    val requiredAuthorities: Set<Authority>

    init {
        this.sessionToken = checkNotNull(sessionToken)
        this.requiredAuthorities = checkNotNull(requiredAuthorities)
    }

    override fun hashCode(): Int {
        return (31 * sessionToken.hashCode())
    }

    override fun toString(): String {
        return "OpenIDCredentials(sessionToken=$sessionToken, requiredAuthorities=$requiredAuthorities)"
    }
}
/** @return The OpenID token */
/** @return The authorities required to successfully authenticate */
