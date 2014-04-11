package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Provider;
import com.google.inject.name.Names;
import javabot.JavabotModule;
import play.Play;
import play.mvc.Http;

public class PlayModule extends JavabotModule {
  @Override
  protected void configure() {
    try {
      Properties props = new Properties();
      InputStream stream = Play.application().resourceAsStream("oauth.json");
      if (stream != null) {
        try {
          ObjectMapper mapper = new ObjectMapper();
          JsonParser parser = mapper.getFactory().createParser(stream);
          JsonNode tree = mapper.readTree(parser);
          props.load(stream);
          bind(String.class)
              .annotatedWith(Names.named("twitter.key"))
              .toInstance(tree.get("twitter").get("key").asText());
          bind(String.class)
              .annotatedWith(Names.named("twitter.secret"))
              .toInstance(tree.get("twitter").get("secret").asText());
          bind(String.class)
              .annotatedWith(Names.named("google.key"))
              .toInstance(tree.get("google").get("client_id").asText());
          bind(String.class)
              .annotatedWith(Names.named("google.secret"))
              .toInstance(tree.get("google").get("client_secret").asText());
        } catch (JsonParseException e) {
          throw new RuntimeException(e.getMessage(), e);
        } catch (IOException e) {
          throw new RuntimeException(e.getMessage(), e);
        } finally {
          stream.close();
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
    // Bind Play request
    bind(Http.Request.class).toProvider(new Provider<Http.Request>() {
      @Override
      public Http.Request get() {
        return Http.Context.current().request();
      }
    });
    // Bind Play response
    bind(Http.Response.class).toProvider(new Provider<Http.Response>() {
      @Override
      public Http.Response get() {
        return Http.Context.current().response();
      }
    });
    // Bind Play Http.Context
    bind(Http.Context.class).toProvider(new Provider<Http.Context>() {
      @Override
      public Http.Context get() {
        return Http.Context.current();
      }
    });
  }

}
