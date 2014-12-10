package javabot.web.model;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public enum InMemoryUserCache {

    // Provide a global singleton for the application
    INSTANCE;

    // A lot of threads will hit this cache
    private volatile Cache<String, User> userCache;

    InMemoryUserCache() {
        reset(15, TimeUnit.MINUTES);
    }

    /**
     * Resets the cache and allows the expiry time to be set (perhaps for testing)
     *
     * @param duration The duration before a user must manually authenticate through a web form due to inactivity
     * @param unit     The {@link java.util.concurrent.TimeUnit} that duration is expressed in
     */
    public InMemoryUserCache reset(int duration, TimeUnit unit) {

        // Build the cache
        if (userCache != null) {
            userCache.invalidateAll();
        }

        // If there is no activity against a key then we want
        // it to be expired from the cache, but each fresh write
        // will reset the expiry timer
        userCache = CacheBuilder
                        .newBuilder()
                        .expireAfterWrite(duration, unit)
                        .maximumSize(1000)
                        .build();

        return INSTANCE;
    }

    /**
     * @param sessionToken The session token to locate the user (not JSESSIONID)
     * @return The matching User or absent
     */
    public Optional<User> getBySessionToken(String sessionToken) {

        // Check the cache
        Optional<User> userOptional = Optional.fromNullable(userCache.getIfPresent(sessionToken));

        if (userOptional.isPresent()) {
            // Ensure we refresh the cache on a check to maintain the session timeout
            userCache.put(sessionToken, userOptional.get());
        }

        return userOptional;

    }

    /**
     * @param user         The User to cache
     */
    public void put(User user) {

        Preconditions.checkNotNull(user);

        userCache.put(user.getSessionToken().toString(), user);
    }

    public void hardDelete(User user) {

        Preconditions.checkNotNull(user);
        Preconditions.checkNotNull(user.getSessionToken());

        userCache.invalidate(user.getSessionToken());
    }

    public Optional<User> getByOpenIDIdentifier(String identifier) {

        Map<String, User> map = userCache.asMap();

        for (Map.Entry<String, User> entry : map.entrySet()) {
            if (entry.getValue().getOpenIDIdentifier().equals(identifier)) {
                return Optional.of(entry.getValue());
            }

        }

        return Optional.absent();
    }

    public Optional<User> getByEmailAddress(String emailAddress) {
        Map<String, User> map = userCache.asMap();

        for (Map.Entry<String, User> entry : map.entrySet()) {

            if (entry.getValue().getEmail().equals(emailAddress)) {
                return Optional.of(entry.getValue());
            }

        }

        return Optional.absent();
    }
}
