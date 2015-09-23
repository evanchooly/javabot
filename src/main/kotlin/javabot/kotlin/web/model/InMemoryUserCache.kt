package javabot.kotlin.web.model

import com.google.common.base.Optional
import com.google.common.base.Preconditions
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import java.util.concurrent.TimeUnit

enum class InMemoryUserCache {

    // Provide a global singleton for the application
    INSTANCE;

    // A lot of threads will hit this cache
    @Volatile
    private var userCache: Cache<String, User>? = null

    init {
        reset(15, TimeUnit.MINUTES)
    }

    /**
     * Resets the cache and allows the expiry time to be set (perhaps for testing)

     * @param duration The duration before a user must manually authenticate through a web form due to inactivity
     * *
     * @param unit     The [TimeUnit] that duration is expressed in
     */
    public fun reset(duration: Int, unit: TimeUnit): InMemoryUserCache {

        // Build the cache
        if (userCache != null) {
            userCache!!.invalidateAll()
        }

        // If there is no activity against a key then we want
        // it to be expired from the cache, but each fresh write
        // will reset the expiry timer
        userCache = CacheBuilder.newBuilder().expireAfterWrite(duration.toLong(), unit).maximumSize(1000).build<String, User>()

        return INSTANCE
    }

    /**
     * @param sessionToken The session token to locate the user (not JSESSIONID)
     * *
     * @return The matching User or absent
     */
    public fun getBySessionToken(sessionToken: String): Optional<User> {

        // Check the cache
        val userOptional = Optional.fromNullable(userCache!!.getIfPresent(sessionToken))

        if (userOptional.isPresent) {
            // Ensure we refresh the cache on a check to maintain the session timeout
            userCache!!.put(sessionToken, userOptional.get())
        }

        return userOptional

    }

    /**
     * @param user         The User to cache
     */
    public fun put(user: User) {

        Preconditions.checkNotNull(user)

        userCache!!.put(user.sessionToken.toString(), user)
    }

    public fun hardDelete(user: User) {

        Preconditions.checkNotNull(user)
        Preconditions.checkNotNull(user.sessionToken)

        userCache!!.invalidate(user.sessionToken)
    }

    public fun getByOpenIDIdentifier(identifier: String?): Optional<User> {

        val map = userCache!!.asMap()

        for (entry in map.entrySet()) {
            if (entry.getValue().openIDIdentifier == identifier) {
                return Optional.of(entry.getValue())
            }

        }

        return Optional.absent<User>()
    }

    public fun getByEmailAddress(emailAddress: String): Optional<User> {
        val map = userCache!!.asMap()

        for (entry in map.entrySet()) {

            if (entry.getValue().email == emailAddress) {
                return Optional.of(entry.getValue())
            }

        }

        return Optional.absent<User>()
    }
}
