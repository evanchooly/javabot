import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "javabot"
  val appVersion      = "5.0.0-SNAPSHOT"

  val appDependencies = Seq(
    "org.webjars" % "webjars-play" % "2.1.0-1",
    "org.webjars" % "bootstrap" % "2.3.1",
    "org.webjars" % "jquery" % "1.9.1",
    "org.webjars" % "less" % "1.3.3",
    "com.antwerkz.javabot" % "core" % appVersion changing(),
    "be.objectify" %% "deadbolt-scala" % "2.1-RC2",
    "com.google.inject.extensions" % "guice-servlet" % "3.0",
    "com.google.inject.extensions" % "guice-multibindings" % "3.0",
    "org.twitter4j" % "twitter4j-core" % "3.0.3",
    "com.google.code.morphia" % "morphia-logging-slf4j" % "0.99",
    "org.pac4j" % "play-pac4j_scala2.10" % "1.1.0",
    "org.pac4j" % "pac4j-oauth" % "1.4.0",
    "com.codiform" % "moo" % "1.3"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository",
    resolvers += Resolver.url("Objectify Play Repository", url("http://schaloner.github.com/releases/"))(Resolver.ivyStylePatterns),
    resolvers += Resolver.url("Objectify Play Snapshot Repository", url("http://schaloner.github.com/snapshots/"))(Resolver.ivyStylePatterns),
    resolvers += "Sonatype snapshots repository" at "https://oss.sonatype.org/content/repositories/public/",
    routesImport ++= Seq("controllers.Binders._", "org.bson.types.ObjectId")


  )
}