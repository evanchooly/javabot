package javabot

import org.jibble.pircbot.PircBot


class Javabot extends PircBot {
  var version: String = "3.0.5";
  var host: String = null
  var port: Int = 0
  var startStrings: Array[String] = null
  var authWait: Int = 0
  var password: String = null
}