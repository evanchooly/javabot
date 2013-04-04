package utils

import play.mvc.Http
import com.google.inject.{AbstractModule, Provider}
import com.google.inject.servlet.{ServletScopes, RequestScoped}

class PlayModule extends AbstractModule {
  protected override def configure()  {
    bindScope(classOf[RequestScoped], ServletScopes.REQUEST)

    // Bind Play request
    bind(classOf[Http.Request]).toProvider(new Provider[Http.Request]() {
      override def get {
        Http.Context.current().request()
      }
    })

    // Bind Play response
    bind(classOf[Http.Response]).toProvider(new Provider[Http.Response]() {
      override def get {
        Http.Context.current().response()
      }
    })

    // Bind Play Http.Context
    bind(classOf[Http.Context]).toProvider(new Provider[Http.Context]() {
      override def get() {
        Http.Context.current()
      }
    })

    configurePlay()
  }

  // Override me
  protected def configurePlay() {
  }
}
