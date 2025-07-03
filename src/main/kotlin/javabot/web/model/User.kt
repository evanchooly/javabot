package javabot.web.model

import com.google.common.collect.Sets
import java.security.Principal
import java.util.HashSet
import java.util.UUID
import org.brickred.socialauth.util.AccessGrant

class User(
    var sessionToken: UUID?,
    var email: String,
    var openIDIdentifier: String,
    var OAuthInfo: AccessGrant,
) : Principal {
    var authorities: HashSet<Authority> = HashSet()

    fun hasAllAuthorities(requiredAuthorities: Set<Authority>): Boolean {
        return authorities.containsAll(requiredAuthorities)
    }

    fun hasAuthority(authority: Authority): Boolean {
        return hasAllAuthorities(Sets.newHashSet(authority))
    }

    override fun getName(): String {
        return email
    }
}
