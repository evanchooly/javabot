import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val name = "javabot"
  val version = "5.1.1"

  val appDependencies = Seq(
    "org.webjars" %% "webjars-play" % "2.2.1-2",
    "org.webjars" % "bootstrap" % "3.1.0",
    "org.webjars" % "jquery" % "2.1.0-2",
    "org.webjars" % "less" % "1.7.0",
    "com.antwerkz.javabot" % "core" % version changing(),
    "be.objectify" %% "deadbolt-java" % "2.2-RC4",
    "com.google.inject" % "guice" % "3.0",
    "com.google.inject.extensions" % "guice-servlet" % "3.0",
    "com.google.inject.extensions" % "guice-multibindings" % "3.0",
    "org.twitter4j" % "twitter4j-core" % "3.0.3",
    "org.mongodb.morphia" % "morphia-logging-slf4j" % "0.105",
    "org.pac4j" % "play-pac4j_java" % "1.2.0",
    "org.pac4j" % "pac4j-oauth" % "1.5.0",
    "com.codiform" % "moo" % "1.3"
  )

  val main = play.Project(name, version, appDependencies).settings(
    resolvers += "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    resolvers += Resolver.url("Objectify Play Repository", url("http://schaloner.github.com/releases/"))(Resolver.ivyStylePatterns),
    resolvers += Resolver.url("Objectify Play Snapshot Repository", url("http://schaloner.github.com/snapshots/"))(Resolver.ivyStylePatterns),
    resolvers += "Sonatype snapshots repository" at "https://oss.sonatype.org/content/repositories/public/",
    routesImport ++= Seq("controllers.Binders._", "org.bson.types.ObjectId")
  )
}
