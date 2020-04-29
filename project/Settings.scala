import play.sbt.PlayImport.guice
import sbt.Keys.{libraryDependencies, organization, scalaVersion, version}
import sbt._
object Settings {

  val circe = "io.circe" %% "circe-yaml" % "0.12.0"

  val commonSettings = Seq(
    libraryDependencies ++= Seq(
      guice, circe
//      "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
    ),
    organization := "br.ufc",
    version := "0.1.0",
    scalaVersion := "2.13.1",
  )
}
