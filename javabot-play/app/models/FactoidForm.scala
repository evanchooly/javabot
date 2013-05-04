package models

import org.bson.types.ObjectId

case class FactoidForm(name: Option[String], value: Option[String], user: Option[String]) {
}