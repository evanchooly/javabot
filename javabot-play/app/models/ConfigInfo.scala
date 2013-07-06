package models

import org.bson.types.ObjectId
import java.util
import javabot.model.Config

case class ConfigInfo(server: String, url: String, port: Int, historyLength: Int, trigger: String,
                      nick: String, password: String) {
}