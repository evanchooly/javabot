package utils

import play.mvc.{SimpleResult, Http, Action}
import com.google.inject.servlet.ServletScopes
import java.util.concurrent.Callable
import java.util
import com.google.inject.Key
import play.libs.F.Promise

case class RequestScopedAction[A](action: Action[A]) extends Action[A] {
  override def call(ctx: Http.Context): Promise[SimpleResult] = {
    ServletScopes.scopeRequest(new Callable[Promise[SimpleResult]] {
      override def call: Promise[SimpleResult] = {
        try {
          delegate.call(ctx)
        } catch {
          case throwable: Throwable => throw new RuntimeException(throwable)
        }
      }
    }, new util.HashMap[Key[_], Object]).call()
  }
}