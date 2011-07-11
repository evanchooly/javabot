package javabot

import org.jibble.pircbot.PircBot
import ca.grimoire.maven.{ArtifactDescription, NoArtifactException}


class Milo extends PircBot {
  val version: String = "Milo " + loadVersion;
  
  val host: String = null
  val port: Int = 0
  val startStrings: Array[String] = null
  val authWait: Int = 0
  val password: String = null
  
  def loadVersion: String = {
      try {
          return ArtifactDescription.locate("javabot", "core").getVersion;
      } catch {
          case nae: NoArtifactException => "UNKNOWN";
      }
  }

}