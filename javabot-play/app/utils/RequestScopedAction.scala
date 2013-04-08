package utils

import play.mvc.{Result, Http, Action}
import com.google.inject.servlet.ServletScopes
import java.util.concurrent.Callable
import java.util
import com.google.inject.Key

class RequestScopedAction extends Action {
  override def call(ctx: Http.Context): Result = {
    ServletScopes.scopeRequest(new Callable[Result] {
      override def call: Result = {
        try {
          delegate.call(ctx)
        } catch {
          case throwable: Throwable => throw new RuntimeException(throwable)
        }
      }
    }, new util.HashMap[Key[_], Object]).call()
  }
}