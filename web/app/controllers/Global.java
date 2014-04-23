package controllers;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Stage;
import com.google.inject.name.Names;
import org.pac4j.core.client.Clients;
import org.pac4j.oauth.client.Google2Client;
import org.pac4j.play.Config;
import play.GlobalSettings;
import security.OAuthDeadboltHandler;
import utils.PlayModule;

public class Global extends GlobalSettings {
  private Injector injector;

  @Override
  public <A> A getControllerInstance(Class<A> controllerClass) {
    return injector.getInstance(controllerClass);
  }

  @Override
  public void onStart(final play.Application app) {
    injector = Guice.createInjector(Stage.PRODUCTION, new PlayModule() {
      @Override
      protected void configure() {
        super.configure();
        bind(play.api.Application.class).toInstance(app.getWrappedApplication());
        requestStaticInjection(OAuthDeadboltHandler.class);
      }
    });
    String key = injector.getInstance(Key.get(String.class, Names.named("google.key")));
    String secret = injector.getInstance(Key.get(String.class, Names.named("google.secret")));
    if (app.isDev()) {
      Config.setClients(new Clients("http://localhost:9000/oauth2callback", new Google2Client(key, secret)));
    } else {
      Config.setClients(new Clients("http://evanchooly.com/oauth2callback", new Google2Client(key, secret)));
    }
    // for test purposes : profile timeout = 60 seconds
    Config.setProfileTimeout(60);
  }
}

