package javabot.web.model

import com.google.common.base.Preconditions
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import java.util.concurrent.TimeUnit.MINUTES

enum class InMemoryUserCache {

    // Provide a global singleton for the application
    INSTANCE;

    // A lot of threads will hit this cache
    @Volatile
    var userCache: Cache<String, User> =
        CacheBuilder.newBuilder()
            .expireAfterWrite(15, MINUTES)
            .maximumSize(1000)
            .build<String, User>()

    /**
     * @param sessionToken The session token to locate the user (not JSESSIONID)
     * *
     * @return The matching User or absent
     */
    fun getBySessionToken(sessionToken: String?): User? {
        if (sessionToken == null) {
            return null
        }
        // Check the cache
        val user = userCache.getIfPresent(sessionToken)
        if (user != null) {
            // Ensure we refresh the cache on a check to maintain the session timeout
            userCache.put(sessionToken, user)
        }
        return user
    }

    /** @param user The User to cache */
    fun put(user: User) {

        Preconditions.checkNotNull(user)

        userCache.put(user.sessionToken.toString(), user)
    }

    fun getByOpenIDIdentifier(identifier: String?): User? {

        val map = userCache.asMap()

        for (entry in map.entries) {
            if (entry.value.openIDIdentifier == identifier) {
                return entry.value
            }
        }

        return null
    }
}
