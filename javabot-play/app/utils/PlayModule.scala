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

class PlayModule extends AbstractModule {
  protected override def configure() {
    try {
      val props = new Properties()
      val stream = Play.application.resourceAsStream("conf/oauth.conf")
      if (stream != null) {
        try {
          props.load(stream)
        } finally {
          stream.close()
        }
      }
      bind(classOf[String])
        .annotatedWith(Names.named("twitter.key"))
        .toInstance(props.getProperty("twitter.key"))

      bind(classOf[String])
        .annotatedWith(Names.named("twitter.secret"))
        .toInstance(props.getProperty("twitter.secret"))

      val file = Files.readFile(Play.application.getFile("conf/operations.list"))

      bind(classOf[List[String]])
        .annotatedWith(Names.named("operations"))
        .toInstance(file.split('\n').toList)

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

  @Provides
  @Named("operations")
  def operations = {
    ""
  }

  @Provides
  def datastore = {
    val mongo = new MongoClient
    val morphia = new Morphia
    morphia.createDatastore(mongo, "javabot")
  }
}
