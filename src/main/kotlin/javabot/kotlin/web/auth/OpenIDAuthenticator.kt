package javabot.kotlin.web.auth

import com.google.common.base.Optional
import io.dropwizard.auth.AuthenticationException
import io.dropwizard.auth.Authenticator
import javabot.kotlin.web.model.InMemoryUserCache
import javabot.kotlin.web.model.User

public class OpenIDAuthenticator : Authenticator<OpenIDCredentials, User> {

    @Throws(AuthenticationException::class)
    override fun authenticate(credentials: OpenIDCredentials): Optional<User> {

        // Get the User referred to by the API key
        val user = InMemoryUserCache.INSTANCE.getBySessionToken(credentials.sessionToken.toString()) ?: return Optional.absent<User>()

        // Check that their authorities match their credentials
        if (!user.hasAllAuthorities(credentials.requiredAuthorities)) {
            return Optional.absent<User>()
        }
        return Optional.of(user)

    }

}