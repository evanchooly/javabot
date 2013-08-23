package controllers

import org.bson.types.ObjectId
import play.api.mvc.Results.Status
import play.api.mvc.{JavascriptLitteral, PathBindable}
import play.api.data.format.Formatter
import play.api.data.FormError
import play.api.http.ContentTypes
import play.api.{Play, PlayException}
import reflect.Manifest


object Binders {

  implicit def bindableOption[T: PathBindable] = new PathBindable[Option[T]] {
    def bind(key: String, value: String) = {
      implicitly[PathBindable[T]].bind(key, value).right.map(Some(_))
    }
    def unbind(key: String, value: Option[T]) = value.map(v => implicitly[PathBindable[T]].unbind(key, v)).getOrElse("")
  }

  implicit def bindableObjectId = new PathBindable[ObjectId] {
    def bind(key: String, value: String) = {
      if (ObjectId.isValid(value)) {
        Right(new ObjectId(value))
      } else {
        Left("Cannot parse parameter " + key + " as BSON ObjectId")
      }
    }

    def unbind(key: String, value: ObjectId) = value.toString
  }

  implicit def bindableJavascriptLiteral = new JavascriptLitteral[ObjectId] {
    def to(value: ObjectId) = value.toString
  }

}