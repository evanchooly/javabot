package models

import org.bson.types.ObjectId
import java.util

case class ConfigInfo(id: ObjectId/*, server: String, url: String, port: Int, historyLength: Int, trigger: String,
                      nick: String, password: String, schemaVersion: Int*/, operations: util.List[String]) {
}