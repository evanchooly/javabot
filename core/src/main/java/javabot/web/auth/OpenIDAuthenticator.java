package javabot.web.auth;

import com.google.common.base.Optional;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import javabot.web.model.InMemoryUserCache;
import javabot.web.model.User;

public class OpenIDAuthenticator implements Authenticator<OpenIDCredentials, User> {

  @Override
  public Optional<User> authenticate(OpenIDCredentials credentials) throws AuthenticationException {

    // Get the User referred to by the API key
      Optional<User> user = InMemoryUserCache.INSTANCE.getBySessionToken(credentials.getSessionToken().toString());
    if (!user.isPresent()) {
      return Optional.absent();
    }

    // Check that their authorities match their credentials
    if (!user.get().hasAllAuthorities(credentials.getRequiredAuthorities())) {
      return Optional.absent();
    }
    return user;

  }

}