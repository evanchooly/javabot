package utils

import play.mvc.Http
import com.google.inject.{AbstractModule, Provider}
import com.google.inject.servlet.{ServletScopes, RequestScoped}

abstract class PlayModule extends AbstractModule {
  protected override def configure()  {
    bindScope(classOf[RequestScoped], ServletScopes.REQUEST)

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

    configurePlay()
  }

  // Override me
  def configurePlay()
}
