package javabot;

import java.util.TreeMap;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class GuiceConfig extends GuiceServletContextListener {
  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new JavabotModule(), new JerseyServletModule() {
      @Override
      protected void configureServlets() {
        serve("/api/*").with(GuiceContainer.class, new TreeMap<String, String>() {{
          put("jersey.config.server.provider.packages", "javabot");
        }});
      }
    });
  }
}
