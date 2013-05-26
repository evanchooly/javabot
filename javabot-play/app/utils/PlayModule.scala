package utils

import com.google.code.morphia.Morphia
import com.google.inject.name.Names
import com.google.inject.{Provides, AbstractModule, Provider}
import com.mongodb.MongoClient
import java.util.Properties
import javax.inject.Named
import play.Play
import play.api.libs.Files
import play.mvc.Http
import java.io.File
import security.OAuthDeadboltHandler
import javabot.dao.util.DateTimeConverter
import javabot.JavabotModule

class PlayModule extends JavabotModule {
  protected override def configure() {
    try {
      val props = new Properties()
      val stream = Play.application.resourceAsStream("oauth.conf")
      if (stream != null) {
        try {
          props.load(stream)
          bind(classOf[String])
            .annotatedWith(Names.named("twitter.key"))
            .toInstance(props.getProperty("twitter.key"))

          bind(classOf[String])
            .annotatedWith(Names.named("twitter.secret"))
            .toInstance(props.getProperty("twitter.secret"))
        } finally {
          stream.close()
        }
      }

      val file: File = Play.application.getFile("conf/operations.list")

      val operations = if(file.exists) Files.readFile(file).split('\n') else new Array[String](0)
      bind(classOf[List[String]])
        .annotatedWith(Names.named("operations"))
        .toInstance(operations.toList)

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

//  @Provides
//  def handler = {
//    new OAuthDeadboltHandler
//  }
}
