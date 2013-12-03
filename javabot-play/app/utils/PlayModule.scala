package utils

import com.google.inject.name.Names
import com.google.inject.Provider
import java.util.Properties
import play.Play
import play.mvc.Http
import javabot.JavabotModule
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}

class PlayModule extends JavabotModule {
  protected override def configure() {
    try {
      val props = new Properties()
      val stream = Play.application.resourceAsStream("oauth.json")
      if (stream != null) {
        try {
          val mapper = new ObjectMapper()
          val parser = mapper.getFactory.createJsonParser(stream)
          val tree: JsonNode = mapper.readTree(parser)
          props.load(stream)
          bind(classOf[String])
            .annotatedWith(Names.named("twitter.key"))
            .toInstance(tree.get("twitter").get("key").asText)

          bind(classOf[String])
            .annotatedWith(Names.named("twitter.secret"))
            .toInstance(tree.get("twitter").get("secret").asText)

          bind(classOf[String])
            .annotatedWith(Names.named("google.key"))
            .toInstance(tree.get("google").get("client_id").asText)

          bind(classOf[String])
            .annotatedWith(Names.named("google.secret"))
            .toInstance(tree.get("google").get("client_secret").asText)
        } finally {
          stream.close()
        }
      }
    }

    // Bind Play request
    bind(classOf[Http.Request]).toProvider(new Provider[Http.Request]() {
      override def get: Http.Request = {
        Http.Context.current().request()
      }
    })

    // Bind Play response
    bind(classOf[Http.Response]).toProvider(new Provider[Http.Response]() {
      override def get: Http.Response = {
        Http.Context.current().response()
      }
    })

    // Bind Play Http.Context
    bind(classOf[Http.Context]).toProvider(new Provider[Http.Context]() {
      override def get(): Http.Context = {
        Http.Context.current()
      }
    })
  }
}
