package utils

import play.api.data._
import play.api.data.format._
import play.api.data.format.Formats._
import play.api.data.Forms._
import org.bson.types.ObjectId

trait ObjectIdFormatter extends Formatter[ObjectId] {

  def bind(key: String, data: Map[String, String]) = {
     Formats.stringFormat.bind(key, data).right.flatMap { s =>
       scala.util.control.Exception.allCatch[ObjectId]
         .either(new ObjectId(s.toString))
         .left.map(e => Seq(FormError(key, "error.objectid", Nil)))
     }
   }

   def unbind(key: String, value: ObjectId) = Map(key -> value.toString)
}
