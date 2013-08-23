package models

import org.bson.types.ObjectId

case class ChannelInfo(id: ObjectId, name: String, key: String, logged: Boolean) {
}