package utils

import javabot.model.Channel
import models.ChannelInfo
import play.api.data.format.{Formats, Formatter}
import org.bson.types.ObjectId
import play.api.data.FormError

object Implicits {
  implicit def from(channel: Channel) = ChannelInfo(channel.getId, channel.getName, channel.getKey, channel.getLogged)

  implicit def objectIdFormat = new Formatter[ObjectId] {
    def bind(key: String, data: Map[String, String]) = {
      Formats.stringFormat.bind(key, data).right.flatMap {
        s =>
          scala.util.control.Exception.allCatch[ObjectId]
            .either(new ObjectId(s.toString))
            .left.map(e => Seq(FormError(key, "error.objectid")))
      }
    }

    def unbind(key: String, value: ObjectId) = Map(key -> value.toString)
  }

}
