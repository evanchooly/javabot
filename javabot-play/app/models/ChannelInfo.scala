package models

import org.bson.types.ObjectId
import javabot.model.Channel

case class ChannelInfo(id: ObjectId, name: String, key: String, logged: Boolean) {
}