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
    "com.antwerkz.javabot" % "core" % appVersion,
    "be.objectify" %% "deadbolt-scala" % "2.1-RC2",
    "com.google.inject.extensions" % "guice-servlet" % "3.0",
    "com.google.inject.extensions" % "guice-multibindings" % "3.0",
    "org.twitter4j" % "twitter4j-core" % "3.0.3"

  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository",
    resolvers += Resolver.url("Objectify Play Repository", url("http://schaloner.github.com/releases/"))(Resolver.ivyStylePatterns),
    resolvers += Resolver.url("Objectify Play Snapshot Repository", url("http://schaloner.github.com/snapshots/"))(Resolver.ivyStylePatterns)
  )

}
