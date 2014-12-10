package javabot.web.auth;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import javabot.web.JavabotConfiguration;
import javabot.web.model.Authority;
import javabot.web.model.InMemoryUserCache;
import javabot.web.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * <p>Injectable to provide the following to {@link RestrictedProvider}:</p> <ul> <li>Performs decode from HTTP request</li> <li>Carries
 * OpenID authentication data</li> </ul>
 *
 * @since 0.0.1
 */
class OpenIDRestrictedToInjectable extends AbstractHttpContextInjectable<User> {

    private static final Logger log = LoggerFactory.getLogger(OpenIDRestrictedToInjectable.class);

    private final Set<Authority> requiredAuthorities;

    /**
     * @param requiredAuthorities The required authorities as provided by the RestrictedTo annotation
     */
    OpenIDRestrictedToInjectable(Authority[] requiredAuthorities) {
        this.requiredAuthorities = Sets.newHashSet(Arrays.asList(requiredAuthorities));
    }

    @Override
    public User getValue(HttpContext httpContext) {

        try {
            final Map<String, Cookie> cookieMap = httpContext.getRequest().getCookies();
            if (!cookieMap.containsKey(JavabotConfiguration.SESSION_TOKEN_NAME)) {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }

            UUID sessionToken = UUID.fromString(cookieMap.get(JavabotConfiguration.SESSION_TOKEN_NAME).getValue());
            final OpenIDCredentials credentials = new OpenIDCredentials(sessionToken, requiredAuthorities);

            Optional<User> user = InMemoryUserCache.INSTANCE.getBySessionToken(credentials.getSessionToken().toString());
            if (!user.isPresent()) {
                throw new WebApplicationException(Status.UNAUTHORIZED);
            }
            if (!user.get().hasAllAuthorities(credentials.getRequiredAuthorities())) {
                throw new WebApplicationException(Status.FORBIDDEN);
            }
            return user.get();
        } catch (IllegalArgumentException e) {
            log.debug("Error decoding credentials", e);
        }

        // Must have failed to be here
        throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }

}