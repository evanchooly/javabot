package javabot.kotlin.web.auth

import com.google.common.base.Objects
import javabot.kotlin.web.model.Authority
import java.util.UUID

/**
 *
 * Value object to provide the following to [OpenIDAuthenticator]:
 *
 *  * Storage of the necessary credentials for OpenID authentication
 *

 * @since 0.0.1
 */
public class OpenIDCredentials
/**
 * @param sessionToken        The session token acting as a surrogate for the OpenID token
 * *
 * @param requiredAuthorities The authorities required to authenticate (provided by the [Restricted] annotation)
 */
(
      sessionToken: UUID,
      requiredAuthorities: Set<Authority>) {

    public val sessionToken: UUID
    public val requiredAuthorities: Set<Authority>

    init {
        this.sessionToken = checkNotNull(sessionToken)
        this.requiredAuthorities = checkNotNull(requiredAuthorities)
    }

    override fun hashCode(): Int {
        return (31 * sessionToken.hashCode())
    }

    override fun toString(): String {
        return Objects.toStringHelper(this).add("sessionId", sessionToken).add("authorities", requiredAuthorities).toString()
    }

}
/**
 * @return The OpenID token
 */
/**
 * @return The authorities required to successfully authenticate
 */
