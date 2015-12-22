package javabot.kotlin.web.model

import com.google.common.base.Preconditions
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import java.util.concurrent.TimeUnit

enum class InMemoryUserCache {

    // Provide a global singleton for the application
    INSTANCE;

    // A lot of threads will hit this cache
    @Volatile
    var userCache: Cache<String, User> = CacheBuilder.newBuilder()
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build<String, User>()

    /**
     * @param sessionToken The session token to locate the user (not JSESSIONID)
     * *
     * @return The matching User or absent
     */
    public fun getBySessionToken(sessionToken: String?): User? {
        // Check the cache
        val user = if (sessionToken != null) userCache.getIfPresent(sessionToken) else null

        if (user != null) {
            // Ensure we refresh the cache on a check to maintain the session timeout
            userCache.put(sessionToken, user)
        }

        return user
    }

    /**
     * @param user         The User to cache
     */
    public fun put(user: User) {

        Preconditions.checkNotNull(user)

        userCache.put(user.sessionToken.toString(), user)
    }

    public fun getByOpenIDIdentifier(identifier: String?): User? {

        val map = userCache.asMap()

        for (entry in map.entries) {
            if (entry.value.openIDIdentifier == identifier) {
                return entry.value
            }

        }

        return null
    }

}
